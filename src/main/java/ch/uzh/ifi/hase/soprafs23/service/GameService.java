package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.StatePattern.GameStateClass;
import ch.uzh.ifi.hase.soprafs23.constant.*;
import ch.uzh.ifi.hase.soprafs23.entityDB.*;
import ch.uzh.ifi.hase.soprafs23.entityDB.Category;
import ch.uzh.ifi.hase.soprafs23.entityOther.Guess;
import ch.uzh.ifi.hase.soprafs23.entityOther.Location;
import ch.uzh.ifi.hase.soprafs23.entityOther.WebsocketPackage;
import ch.uzh.ifi.hase.soprafs23.repository.*;
import ch.uzh.ifi.hase.soprafs23.rest.dto.GamePostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.Random;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static ch.uzh.ifi.hase.soprafs23.constant.GameState.SETUP;
import static java.lang.Boolean.TRUE;

@Service
@Transactional
public class GameService {
    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final GameRepository gameRepository;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final Map<Long, ScheduledFuture<?>> scheduledFutures = new ConcurrentHashMap<>();
    private final CountryService countryService;
    private final CountryRepository countryRepository;
    private final UserRepository userRepository;
    private final GameUserRepository gameUserRepository;
    private final CategoryStackRepository categoryStackRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final Map<Long, Game> activeGames = Collections.synchronizedMap(new HashMap<>());
    private final Object lock = new Object();

    @Autowired
    public GameService(@Qualifier("gameRepository") GameRepository gameRepository, @Qualifier("countryRepository") CountryRepository countryRepository, @Qualifier("userRepository") UserRepository userRepository, CountryService countryService, SimpMessagingTemplate messagingTemplate, @Qualifier("gameUserRepository") GameUserRepository gameUserRepository, @Qualifier("categoryStackRepository")CategoryStackRepository categoryStackRepository) {
        this.gameRepository = gameRepository;
        this.countryService = countryService;
        this.countryRepository = countryRepository;
        this.userRepository = userRepository;
        this.messagingTemplate = messagingTemplate;
        this.gameUserRepository = gameUserRepository;
        this.categoryStackRepository = categoryStackRepository;
    }
    public Game createGame(GamePostDTO gamePostDTO) {

        Game game = new Game();

        Long lobbyCreatorUserId = Long.parseLong(gamePostDTO.getLobbyCreatorUserId());
        User lobbyCreatorUser = userRepository.findByUserId(lobbyCreatorUserId);
        GameUser lobbyCreator = GameUser.transformUserToGameUser(lobbyCreatorUser, game);

        //Set GameID which is now generated randomly
        game.setGameId(generateGameID());

        //Set participants
        Set<GameUser> gameUsers = new HashSet<>();
        gameUsers.add(lobbyCreator);
        game.setParticipants(gameUsers);

        List<RegionEnum> selectedRegions = gamePostDTO.getSelectedRegions();
        game.setSelectedRegions(selectedRegions);

        Set<Long> countryIds = getCountryIdsByRegionsAndDifficulty(gamePostDTO.getSelectedRegions(), gamePostDTO.getDifficulty());
        game.setCountriesToPlayIds(countryIds);
       game.setLobbyCreator(lobbyCreator);
       game.setDifficulty(gamePostDTO.getDifficulty());
       game.setGameMode(gamePostDTO.getGameMode());
       game.setTimeBetweenRounds(gamePostDTO.getTimeBetweenRounds());
       game.setNumberOfGuesses(gamePostDTO.getNumberOfGuesses());

        //Set SETUP State
        game.setCurrentState(SETUP);

        //Set Category Stack
        game.setCategoryStack(gamePostDTO.getCategoryStack());

        //Set game round duration and number of rounds
        game.setRoundDuration(gamePostDTO.getRoundDuration());
        game.setNumberOfRounds(gamePostDTO.getNumberOfRounds());

        game.setOpenLobby(gamePostDTO.isOpenLobby());

        activeGames.put(game.getGameId(), game);
        updateGameState(game.getGameId(), WebsocketType.GAMESTATEUPDATE, game.getCurrentState());
        return game;
    }

    public Game joinGame(Long gameId, Long userId) {
        synchronized (lock) {
            Game game = activeGames.get(gameId);
            User user = userRepository.findByUserId(userId);
            GameUser gameUser = GameUser.transformUserToGameUser(user, game);

            Set<GameUser> participants = new HashSet<>(game.getParticipants());

            for (GameUser gameUser1 : participants) {
                if (gameUser1.getUserId().equals(userId)) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already in game");
                }
            }

            participants.add(gameUser);
            game.setParticipants(participants);
            updateGameState(game.getGameId(), WebsocketType.PLAYERUPDATE, game.getParticipants());
            return game;
        }
    }

    public Game startGame(Long gameId) {
        synchronized (lock) {
            Game game = activeGames.get(gameId);
            if (game == null) {
                throw new RuntimeException("Game not found");
            }

            game.setCurrentState(SETUP);
            GameState currentGameState = game.getCurrentState();
            GameStateClass currentGameStateClass = Game.getGameStateClass(currentGameState);
            currentGameStateClass.updateGameEverySecond(game, this);
            //Initialize the gameUpdating thread
            String topic = "/topic/game/" + gameId;
            GameUpdater gameUpdater = new GameUpdater(gameId, topic);
            ScheduledFuture<?> scheduledFuture = scheduler.scheduleAtFixedRate(gameUpdater, 0, 1, TimeUnit.SECONDS);
            scheduledFutures.put(gameId, scheduledFuture);
            return game;
        }
    }

    private boolean checkIfEveryoneGuessed(Game game) {
        boolean haveAllGuessed = true;
        Set<GameUser> participants = game.getParticipants();
        for (GameUser participant : participants) {
            if (participant.getNumberOfGuessesLeft() > 0 && !participant.isGuessedCorrectly()) {
                haveAllGuessed = false;
                break;
            }
        }
        return haveAllGuessed;
    }


    public String submitGuess(Long gameId, Guess guess) {
        synchronized (lock) {
            try {
                Game game = getActiveGame(gameId);
                Set<GameUser> gameUsers = new HashSet<>(game.getParticipants());
                GameUser gameUser = findGameUser(gameUsers, guess.getUserId());
                if (gameUser.getNumberOfGuessesLeft() <= 0 || gameUser.isGuessedCorrectly()){
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You cannot submit a Guess");
                }

                gameUser.setNumberOfGuessesLeft(gameUser.getNumberOfGuessesLeft() - 1);

                String returnString = "";
                if (countryRepository.findNameByCountryId(game.getCurrentCountryId()).equals(guess.getGuess())) {
                    gameUser.setGamePoints(game.getRemainingRoundPoints() + gameUser.getGamePoints());
                    gameUser.setGuessedCorrectly(true);
                    returnString = "Your guess was right you get " + game.getRemainingRoundPoints() + " points";
                }
                else {
                    returnString = "Your guess was wrong";
                }

                if (checkIfEveryoneGuessed(game)) {
                    if (game.getRemainingRounds() == 0) {
                        game.setRemainingTime(30L);
                        game.setCurrentState(GameState.ENDED);
                    }
                    else {
                        game.setRemainingTime(game.getTimeBetweenRounds());
                        game.setCurrentState(GameState.SCOREBOARD);
                    }
                    updateGameState(game.getGameId(), WebsocketType.GAMEUPDATE, DTOMapper.INSTANCE.convertEntityToGameGetDTO(game));
                }
                game.setParticipants(gameUsers);
                updateGameState(gameId, WebsocketType.PLAYERUPDATE, game.getParticipants());
                return returnString;
            }
            catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.toString());
            }
        }
    }

    private void updateGameEverySecond(Long gameId) {
        synchronized (lock) {
            Game game = activeGames.get(gameId);
            GameState currentGameState = game.getCurrentState();
            GameStateClass currentGameStateClass = Game.getGameStateClass(currentGameState);
            currentGameStateClass.updateGameEverySecond(game, this);
        }
    }


    public String getGameCountryName(Long gameId) {
        Game game = activeGames.get(gameId);
        return countryRepository.findNameByCountryId(game.getCurrentCountryId());
    }


    private class GameUpdater implements Runnable {
        private final Long gameId;

        public GameUpdater(Long gameId, String topic) {
            this.gameId = gameId;
        }

        @Override
        public void run() {
            try {
                updateGameEverySecond(gameId);
            }
            catch (Exception e) {
                log.error("Error during game update execution", e);
            }
        }
    }

        public void stopGame(Long gameId) {
            synchronized (lock) {
                ScheduledFuture<?> scheduledFuture = scheduledFutures.get(gameId);
                if (scheduledFuture != null) {
                    scheduledFuture.cancel(false);
                    scheduledFutures.remove(gameId);
                }
            }
        }

    public Set<Long> getCountryIdsByRegionsAndDifficulty(List<RegionEnum> regions, Difficulty difficulty) {
        Long minPopulation = countryService.getMinPopulationByDifficulty(difficulty);
        Page<Country> countriesPage = countryRepository.getCountriesByRegionsAndDifficulty(regions, minPopulation, PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.DESC, "population")));
        return countriesPage.getContent().stream().map(Country::getCountryId).collect(Collectors.toSet());
    }

    public List<Game> getGames() {
        List<Game> games = gameRepository.findAll();
        games.addAll(activeGames.values());
        return games;
    }
    public List<Game> getOpenLobbyGames() {
        return activeGames.values().stream().filter(game -> game.getOpenLobby()).collect(Collectors.toList());

    }
    public List<Game> getOpenPlayableLobbyGames() {
        List<Game> OpenGames = activeGames.values().stream().filter(game -> game.getOpenLobby()).collect(Collectors.toList());
        List<Game> JoinableGames = new ArrayList<>();
        for (Game game : OpenGames)
        {
            if (game.getCurrentState().equals(SETUP))
            {
                JoinableGames.add(game);
                log.debug("Game equal setup", game);
            }
            else
            {
                log.debug("Game not equal setup", game);

               // OpenGames.remove(game);
            }
        }
        //return this.gameRepository.findByCurrentStateIs(SETUP);

        return JoinableGames;
    }
    public Game getQuickGame(){
        List<Game> OpenGames = activeGames.values().stream().filter(game -> game.getOpenLobby()).collect(Collectors.toList());
        Game QuickGame = OpenGames.get(0);
        for (Game game : OpenGames)
        {

            if (game.getParticipants().size()>QuickGame.getParticipants().size())
            {
                QuickGame = game;
            }
        }

        return QuickGame;
    }

    public Game getGame(Long gameId) {
        Game game = activeGames.get(gameId);
        if (game == null) {
            game = gameRepository.findByGameId(gameId);
        }
        if (game == null) {
            throw new RuntimeException("Game not found with gameId: " + gameId);
        }
        return game;
    }

    public Game getActiveGame(Long gameId) {
        return activeGames.get(gameId);
    }

    private GameUser findGameUser(Set<GameUser> gameUsers, Long userId){
        for (GameUser gameUser : gameUsers) {
            if (gameUser.getUserId().equals(userId)) {
                return gameUser;
            }
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is not in game");
    }

    public List<String> getGameCountriesNames(Long gameId) {
        Game game = activeGames.get(gameId);
        List<String> countryNames = new ArrayList<>();
        Set<Long> countryIds = game.getCountriesToPlayIds();
        for (Long countryId : countryIds) {
            countryNames.add(countryRepository.findNameByCountryId(countryId));
        }
        return countryNames;
    }

    public Category transformToCategory(CategoryEnum type, Long countryId) {
        Category category = new Category();
        category.setType(type);
        switch (type) {
            case POPULATION -> {
                category.setPopulation(countryRepository.findPopulationByCountryId(countryId));
                return category;
            }
            case OUTLINE -> {
                Outline outline = countryRepository.findOutlineByCountryId(countryId);
                category.setOutline(outline.getOutline());
                return category;
            }
            case FLAG -> {
                category.setFlag(countryRepository.findFlagByCountryId(countryId));
                return category;
            }
            case LOCATION -> {
                Location location = countryRepository.findLocationByCountryId(countryId);
                category.setLocation(location);
                return category;
            }
            case CAPITAL -> {
                category.setCapital(countryRepository.findCapitalByCountryId(countryId));
                return category;
            }
            default -> {
                return null;
            }
        }
    }

    public void updateGameState(Long gameId, WebsocketType websocketType, Object websocketParam) {
        try{
            WebsocketPackage websocketPackage = new WebsocketPackage(websocketType, websocketParam);
            messagingTemplate.convertAndSend("/topic/games/" + gameId, websocketPackage);
        }catch (Exception e){
        }

    }

    private void updatePlayerState(Long gameUserId, Long gameId, WebsocketType websocketType, Object websocketParam){
        try {
            WebsocketPackage websocketPackage = new WebsocketPackage(websocketType, websocketParam);
            messagingTemplate.convertAndSend("/topic/games/" + gameId + "/" + gameUserId, websocketPackage);
        }catch (Exception e){
        }
    }

    public long generateGameID(){
        //this function creates an ID between 10000 and 99999 to define the game id
        Random rand = new Random();
        long rndNumber = rand.nextInt(89999);
        long GameID= rndNumber+10000;

        //Now we check if it is already used by another name:
        if(checkGeneratedGameID(GameID)){
            return GameID;
        }
        else {
            //if the GameID is already used, the function is called again
            generateGameID();
        }
        return GameID;
    }
    public boolean checkGeneratedGameID(long gameID) {
        try {
            Game foundGame = activeGames.get(gameID);
            if (foundGame == null) {
                foundGame = gameRepository.findByGameId(gameID);
            }
            if (foundGame != null && foundGame.getGameId() == gameID) {
                return false;
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public void leaveGame(Long gameId, Long userId) {
        synchronized (lock) {
            Game game = activeGames.get(gameId);
            if (game == null) {
                return;
            }

            Set<GameUser> participants = game.getParticipants();
            GameUser gameUser = findGameUser(participants, userId);
            if (game.getCurrentState() == GameState.ENDED) {
                gameUser.setHasLeft(true);
                boolean everyOneWantsToPlayAgain = true;

                for (GameUser participant : participants) {
                    if (!participant.isUserPlayingAgain() && !participant.isHasLeft()) {
                        everyOneWantsToPlayAgain = false;
                    }
                }
                if (everyOneWantsToPlayAgain) {
                    createRestartedGame(game);
                    stopGame(gameId);
                    saveGameToDB(game);
                }
            }
            else {
                participants.remove(gameUser);
                if (participants.isEmpty()) {
                    stopGame(gameId);
                    activeGames.remove(gameId);
                    return;
                }
                if (game.getLobbyCreator() == gameUser && !participants.isEmpty()) {
                    game.setLobbyCreator(participants.iterator().next());
                }

                if (checkIfEveryoneGuessed(game)) {
                    if (game.getRemainingRounds() == 0) {
                        game.setRemainingTime(30L);
                        game.setCurrentState(GameState.ENDED);
                    }
                    else {
                        game.setRemainingTime(game.getTimeBetweenRounds());
                        game.setCurrentState(GameState.SCOREBOARD);
                    }
                }
            }
            game.setParticipants(participants);
            updateGameState(game.getGameId(), WebsocketType.GAMEUPDATE, DTOMapper.INSTANCE.convertEntityToGameGetDTO(game));
        }
    }

    public void saveGameToDB(Game game) {
        activeGames.remove(game.getGameId());
        for(GameUser gameUser : game.getParticipants()) {
            gameUserRepository.save(gameUser);
        }
        gameRepository.save(game);
    }

    public Game addUserToRestart(Long gameId, Long userId){
        synchronized (lock) {
            Game game = activeGames.get(gameId);
            if (game == null) {
                throw new RuntimeException("Game not found with gameId: " + gameId);
            }
            Set<GameUser> participants = game.getParticipants();
            boolean hasBeenAdded = false;
            for (GameUser participant : participants) {
                if (participant.getUserId().equals(userId)) {
                    participant.setUserPlayingAgain(true);
                    hasBeenAdded = true;
                }
            }
            if (!hasBeenAdded) {
                throw new RuntimeException("User not found in game with gameId: " + gameId);
            }
            game.setParticipants(participants);

            boolean everyOneWantsToPlayAgain = true;
            for (GameUser participant : participants) {
                if (!participant.isUserPlayingAgain() && !participant.isHasLeft()) {
                    everyOneWantsToPlayAgain = false;
                }
            }
            if (everyOneWantsToPlayAgain) {
                createRestartedGame(game);
                stopGame(gameId);
                saveGameToDB(game);
            }
            updateGameState(gameId, WebsocketType.PLAYERUPDATE, participants);
            return game;
        }
    }

    public void createRestartedGame(Game game){
        Set<GameUser> playersPlayingAgain = new HashSet<>();
        for(GameUser participant: game.getParticipants()){
            if(participant.isUserPlayingAgain() && !participant.isHasLeft()){
                playersPlayingAgain.add(participant);
            }
        }
        if(!playersPlayingAgain.isEmpty()){
            GamePostDTO gamePostDTO = new GamePostDTO();
            gamePostDTO.setLobbyCreatorUserId(determineNewHost(game));
            gamePostDTO.setRoundDuration(game.getRoundDuration());
            gamePostDTO.setNumberOfRounds(game.getNumberOfRounds());
            gamePostDTO.setCategoryStack(game.getCategoryStack());
            gamePostDTO.setSelectedRegions(game.getSelectedRegions());
            gamePostDTO.setOpenLobby(game.getOpenLobby());
            gamePostDTO.setDifficulty(game.getDifficulty());
            gamePostDTO.setGameMode(game.getGameMode());
            gamePostDTO.setTimeBetweenRounds(game.getTimeBetweenRounds());
            gamePostDTO.setNumberOfGuesses(game.getNumberOfGuesses());

            Game newGame = createGame(gamePostDTO);
            newGame.setLobbyCreator(findGameUser(playersPlayingAgain, Long.parseLong(gamePostDTO.getLobbyCreatorUserId())));
            newGame.setParticipants(new HashSet<GameUser>());
            addParticipantsToNewGame(game, newGame);
            game.setNextGameId(newGame.getGameId());
            updateGameState(game.getGameId(), WebsocketType.GAMEUPDATE, DTOMapper.INSTANCE.convertEntityToGameGetDTO(game));
        }
    }

    private void addParticipantsToNewGame(Game oldGame, Game newGame){
        Set<GameUser> newParticipants = newGame.getParticipants();
        for(GameUser participant: oldGame.getParticipants()){
            if(participant.isUserPlayingAgain()&&!participant.isHasLeft()){
                User user = userRepository.findByUserId(participant.getUserId());
                GameUser gameUser = GameUser.transformUserToGameUser(user, newGame);
                newParticipants.add(gameUser);
            }
        }
    }

    private String determineNewHost(Game game){
        Long newHostId = null;
        for(GameUser participant: game.getParticipants()){
            if(participant.isUserPlayingAgain() && !participant.isHasLeft()){
                newHostId = participant.getUserId();
                if(participant.getUserId().equals(game.getLobbyCreator().getUserId())){
                    return newHostId.toString();
                }
            }
        }
        return newHostId.toString();
    }

    public void setNewClosestCountries(Game game){
        Long currentCountryId = game.getCurrentCountryId();
        List<Object[]> countryIdAndLocationList = countryRepository.findCountryIdAndLocation();
        Map<Long, Location> countryIdLocationMap = new HashMap<>();

        for (Object[] countryIdAndLocation : countryIdAndLocationList) {
            Long countryId = (Long) countryIdAndLocation[0];
            Location location = (Location) countryIdAndLocation[1];
            countryIdLocationMap.put(countryId, location);
        }
        List<Long> closestCountryIds = calculateClosestCountries(currentCountryId, countryIdLocationMap);
        List<String> closestCountryNames = new ArrayList<>();
        for(Long countryId : closestCountryIds){
            closestCountryNames.add(countryRepository.findNameByCountryId(countryId));
        }
        Collections.shuffle(closestCountryNames);
        CategoryStack categoryStack = game.getCategoryStack();
        categoryStack.setClosestCountries(closestCountryNames);
    }

    private List<Long> calculateClosestCountries(Long currentCountryId, Map<Long, Location> countryIdLocationMap){
        List<Long> closestCountries = new ArrayList<>();
        Location currentCountryLocation = countryIdLocationMap.get(currentCountryId);
        Map<Long, Long> countryIdDistanceMap = new HashMap<>();

        for (Map.Entry<Long, Location> entry : countryIdLocationMap.entrySet()) {
            Long countryId = entry.getKey();
            Location location = entry.getValue();
            countryIdDistanceMap.put(countryId, calculateDistance(currentCountryLocation, location));
        }

        List<Map.Entry<Long, Long>> sortedEntries = new ArrayList<>(countryIdDistanceMap.entrySet());
        sortedEntries.sort(Map.Entry.comparingByValue());

        List<Long> firstSixCountryIds = sortedEntries.stream()
                .limit(6)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        return firstSixCountryIds;
    }

    private Long calculateDistance(Location location1, Location location2){
        return Math.round(Math.sqrt(Math.pow(location1.getLatitude() - location2.getLatitude(), 2) + Math.pow(location1.getLongitude() - location2.getLongitude(), 2)));
    }

}