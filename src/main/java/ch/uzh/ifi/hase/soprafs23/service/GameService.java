package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.CategoryEnum;
import ch.uzh.ifi.hase.soprafs23.constant.GameState;
import ch.uzh.ifi.hase.soprafs23.constant.WebsocketType;
import ch.uzh.ifi.hase.soprafs23.entityDB.*;
import ch.uzh.ifi.hase.soprafs23.entityOther.Category;
import ch.uzh.ifi.hase.soprafs23.entityOther.Guess;
import ch.uzh.ifi.hase.soprafs23.entityOther.Location;
import ch.uzh.ifi.hase.soprafs23.entityOther.WebsocketPackage;
import ch.uzh.ifi.hase.soprafs23.repository.CountryRepository;
import ch.uzh.ifi.hase.soprafs23.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs23.rest.dto.GamePostDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.concurrent.*;

@Service
@Transactional
public class GameService {
    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final GameRepository gameRepository;

    private final MyHandler myHandler;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final Map<Long, ScheduledFuture<?>> scheduledFutures = new ConcurrentHashMap<>();

    private final CountryService countryService;
    private final CountryRepository countryRepository;
    private final UserRepository userRepository;


    @Autowired
    public GameService(@Qualifier("gameRepository") GameRepository gameRepository, @Qualifier("countryRepository") CountryRepository countryRepository, @Qualifier("userRepository") UserRepository userRepository, CountryService countryService, MyHandler myHandler) {
        this.gameRepository = gameRepository;
        this.countryService = countryService;
        this.countryRepository = countryRepository;
        this.userRepository = userRepository;
        this.myHandler = myHandler;
    }

    public Game createGame(GamePostDTO gamePostDTO) {
        Game game = new Game();

        Long lobbyCreatorUserId = Long.parseLong(gamePostDTO.getLobbyCreatorUserId());
        User lobbyCreatorUser = userRepository.findByUserId(lobbyCreatorUserId);
        GameUser lobbyCreator = GameUser.transformUserToGameUser(lobbyCreatorUser);

        //Set participants
        Set<GameUser> gameUsers = new HashSet<>();
        gameUsers.add(lobbyCreator);
        game.setParticipants(gameUsers);

        Set<Long> countreiss = countryRepository.getAllCountryIds();
        System.out.println(countreiss);
        //Set Countries to Play
        game.setCountriesToPlayIds(countreiss);
        game.setLobbyCreatorUserId(lobbyCreator.getUserId());

        //Set SETUP State
        game.setCurrentState(GameState.SETUP);

        //Set Category Stack
        CategoryStack categoryStack = new CategoryStack();
        categoryStack.addAll(Arrays.asList(CategoryEnum.values()));
        game.setCategoryStack(categoryStack);

        //Set game round duration and number of rounds
        game.setRoundDuration(gamePostDTO.getRoundSeconds());
        game.setNumberOfRounds(gamePostDTO.getNumberOfRounds());

        game.setRandomizedHints(gamePostDTO.isRandomizedHints());
        game.setOpenLobby(gamePostDTO.isOpenLobby());

        gameRepository.saveAndFlush(game);
        updateGameState(game.getGameId(), WebsocketType.GAMESTATEUPDATE, game.getCurrentState());
        System.out.println(game);
        return game;
    }

    public Game joinGame(Long gameId, Long userId) {
        Game game = gameRepository.findByGameId(gameId);
        User user = userRepository.findByUserId(userId);
        GameUser gameUser = GameUser.transformUserToGameUser(user);
        Set<GameUser> participants = game.getParticipants();
        for (GameUser participant : participants) {
            if (participant.getUserId().equals(userId)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is already in game");
            }
        }
        participants.add(gameUser);
        gameRepository.saveAndFlush(game);
        updateGameState(game.getGameId(), WebsocketType.GAMESTATEUPDATE, game.getCurrentState());
        return game;
    }

    public Game startGame(Long gameId) {
        Game game = gameRepository.findByGameId(gameId);
        game.setCurrentState(GameState.GUESSING);
        game.setRemainingRoundPoints(100L);
        Long initialCountryId = countryService.getAllCountryIdsWithRandomId();
        game.setCurrentCountryId(initialCountryId);
        game.setRemainingTime(game.getRoundDuration());
        game.setRemainingRounds(game.getNumberOfRounds()-1);

        final Game game2 = gameRepository.saveAndFlush(game);

        updateGameState(gameId, WebsocketType.GAMESTATEUPDATE, game.getCurrentState());

        String topic = "/topic/game/" + game2.getGameId();
        GameUpdater gameUpdater = new GameUpdater(game2.getGameId(), topic);

        ScheduledFuture<?> scheduledFuture = scheduler.scheduleAtFixedRate(gameUpdater, 1, 1, TimeUnit.SECONDS);

        scheduledFutures.put(gameId, scheduledFuture);

        return game;
    }

    //Logic fixed; not tested
    public String submitGuess(Long gameId, Guess guess) {
        try {
            System.out.println("THe guess username: "+ guess.getUserId());
            System.out.println("The guess submitted is:" + guess.getGuess());
            Game game = gameRepository.findByGameId(gameId);
            Set<GameUser> gameUsers = game.getParticipants();
            GameUser gameUser = findGameUser(gameUsers, guess.getUserId());
            gameUser.setCurrentState(GameState.SCOREBOARD);
            updatePlayerState(gameUser,WebsocketType.PLAYERUPDATE, GameState.SCOREBOARD);

            String returnString = "";
            if (countryRepository.findNameByCountryId(game.getCurrentCountryId()).equals(guess.getGuess())) {
                gameUser.setGamePoints(game.getRemainingRoundPoints());
                returnString = "Your guess was right you get " + game.getRemainingRoundPoints() + " points";
                System.out.println("The user " + gameUser.getUsername() + " got " + gameUser.getGamePoints() + " points");
            }
            else {
                gameUser.setGamePoints(0L);
                returnString = "Your guess was wrong you get 0 points";
            }
            boolean haveAllGuessed = true;
            Set<GameUser> participants = game.getParticipants();
            for(GameUser participant: participants){
                if (participant.getCurrentState() != GameState.SCOREBOARD) {
                    haveAllGuessed = false;
                    break;
                }
            }
            if(haveAllGuessed){
                game.setCurrentState(GameState.SCOREBOARD);
                updateGameState(game.getGameId(),  WebsocketType.GAMESTATEUPDATE, GameState.SCOREBOARD);
            }
            return returnString;
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.toString());
        }

    }

    private void updateGameEverySecond(Long gameId) {
        System.out.println("Updating game every second");
        // get the latest game state from the GameRepository
        Game game = gameRepository.findByGameId(gameId);
        // decrement the time remaining by 1 second
        Long timeRemaining = game.getRemainingTime();

        // if the time remaining is 0 and there are still rounds left, start a new round
        if (timeRemaining == 0 && game.getRemainingRounds() >0){
            game.setRemainingRounds(game.getRemainingRounds()- 1);
            game.setRemainingTime(game.getRoundDuration());
            game.setRemainingRoundPoints(100L);
            game.getCategoryStack().refillStack();
            game.setCategoryStack(game.getCategoryStack());
            game.setCurrentCountryId(countryService.getAllCountryIdsWithRandomId());

        }
        // if the time remaining is 0 and there are no rounds left, go to the scoreboard
        else if (timeRemaining == 0 && game.getRemainingRounds() == 0) {
            game.setCurrentState(GameState.SCOREBOARD);
            updateGameState(gameId, WebsocketType.GAMESTATEUPDATE, GameState.SCOREBOARD);
            stopGame(game.getGameId());
        }
        // if the time remaining is greater than 0 and the game is in the guessing state, decrement the time remaining and update the game
        else if (timeRemaining > 0 && game.getCurrentState() == GameState.GUESSING){
            game.setRemainingTime(timeRemaining - 1);
            updateGameState(gameId, WebsocketType.TIMEUPDATE, game.getRemainingTime());

            reduceCurrentPoints(game);

            if (timeRemaining % ((int) (game.getRoundDuration() / 5)) == 0) {
                CategoryStack remainingCategories = game.getCategoryStack();
                if (!remainingCategories.isEmpty()) {
                    System.out.println("Remaining categories: " + remainingCategories);

                    CategoryEnum currentCategoryEnum = remainingCategories.pop();
                    remainingCategories.setCurrentCategory(currentCategoryEnum);
                    Category category = transformToCategory(currentCategoryEnum, game.getCurrentCountryId());

                    updateGameState(gameId, WebsocketType.CATEGORYUPDATE, category);
                }
            }
        }
        gameRepository.saveAndFlush(game);
    }

    private void reduceCurrentPoints(Game game) {

        long roundTime = game.getRoundDuration();
        double pointsDeducted = 100.0 / roundTime;
        Long pDeducted = (long) Math.floor(pointsDeducted);
        Long newCurrentPoints = game.getRemainingRoundPoints() - pDeducted;
        game.setRemainingRoundPoints(newCurrentPoints);
        updateGameState(game.getGameId(), WebsocketType.POINTSUPDATE, newCurrentPoints);
    }

    public String getGameCountryName(Long gameId) {
        Game game = gameRepository.findByGameId(gameId);
        return countryRepository.findNameByCountryId(game.getCurrentCountryId());
    }

    public Game getGameById(Long id) {
        return gameRepository.findByGameId(id);
    }



    public Game getGameByIdAndAuth(Long id, String token) {
        Game game = gameRepository.findByGameId(id);
        for (GameUser gameUser : game.getParticipants()) {
            if (gameUser.getToken().equals(token)) {
                return game;
            }
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not authorized to view this game, press the button to join");
    }

    private void sendWebsocketPackageToLobby(Long gameId, WebsocketPackage websocketPackage) {
        Game game = gameRepository.findByGameId(gameId);
        for (GameUser gameUser : game.getParticipants()) {
            System.out.println("Sending websocket package to user: " + gameUser.getUsername());
            myHandler.sendWebsocketPackage(gameUser.getToken(), websocketPackage);
        }
    }

    private void sendWebsocketPackageToPlayer(GameUser gameUser, WebsocketPackage websocketPackage) {
        System.out.println("Sending websocket package to user: " + gameUser.getUsername());
        myHandler.sendWebsocketPackage(gameUser.getToken(), websocketPackage);
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
            System.out.println("Stopping game");
            ScheduledFuture<?> scheduledFuture = scheduledFutures.get(gameId);
            if (scheduledFuture != null) {
                scheduledFuture.cancel(false);
                scheduledFutures.remove(gameId);
            }
        }
    public List<Game> getGames() {
        return this.gameRepository.findAll();
    }

    public Game getGame(Long gameId) {
        return this.gameRepository.findByGameId(gameId);
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
        Game game = gameRepository.findByGameId(gameId);
        List<String> countryNames = new ArrayList<>();
        Set<Long> countryIds = game.getCountriesToPlayIds();
        for (Long countryId : countryIds) {
            countryNames.add(countryRepository.findNameByCountryId(countryId));
        }
        return countryNames;
    }

    private Category transformToCategory(CategoryEnum type, Long countryId) {
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

    private void updateGameState(Long gameId, WebsocketType websocketType, Object websocketParam) {
        WebsocketPackage websocketPackage = new WebsocketPackage(websocketType, websocketParam);
        sendWebsocketPackageToLobby(gameId, websocketPackage);
    }

    private void updatePlayerState(GameUser gameUser, WebsocketType websocketType, Object websocketParam){
        WebsocketPackage websocketPackage = new WebsocketPackage(websocketType, websocketParam);
        sendWebsocketPackageToPlayer(gameUser, websocketPackage);
    }
}