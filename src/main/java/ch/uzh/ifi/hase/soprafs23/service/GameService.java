package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.CategoryEnum;
import ch.uzh.ifi.hase.soprafs23.constant.GameState;
import ch.uzh.ifi.hase.soprafs23.constant.WebsocketType;
import ch.uzh.ifi.hase.soprafs23.entityDB.*;
import ch.uzh.ifi.hase.soprafs23.entityOther.*;
import ch.uzh.ifi.hase.soprafs23.repository.CategoryStackRepository;
import ch.uzh.ifi.hase.soprafs23.repository.CountryRepository;
import ch.uzh.ifi.hase.soprafs23.repository.GameRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.concurrent.*;

@Service
@Transactional
public class GameService {
    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final Map<Long, List<String>> subscribersByGameId = new ConcurrentHashMap<>();

    private final GameRepository gameRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final Map<Long, ScheduledFuture<?>> scheduledFutures = new ConcurrentHashMap<>();

    private final CountryService countryService;
    private final CountryRepository countryRepository;



    @Autowired
    public GameService(@Qualifier("gameRepository")GameRepository gameRepository, @Qualifier("countryRepository") CountryRepository countryRepository, SimpMessagingTemplate messagingTemplate, CountryService countryService){
        this.gameRepository = gameRepository;
        this.messagingTemplate = messagingTemplate;
        this.countryService = countryService;
        this.countryRepository = countryRepository;
    }

    public List<Game> getGames(){
        return this.gameRepository.findAll();
    }

    public List<String> getGameCountriesNames(Long gameId){
        Game game = gameRepository.findByGameId(gameId);
        List<String> countryNames = new ArrayList<>();
        Set<Long> countryIds = game.getCountriesToPlayIds();
        for(Long countryId : countryIds){
            countryNames.add(countryRepository.findNameByCountryId(countryId));
        }
        return countryNames;
    }

    private Category transformToCategory(CategoryEnum type, Long countryId){
        Category category = new Category();
        category.setType(type);
        switch (type){

            case POPULATION:
                category.setPopulation(countryRepository.findPopulationByCountryId(countryId));
                return category;
            case OUTLINE:
                Outline outline =  countryRepository.findOutlineByCountryId(countryId);
                category.setOutline(outline.getOutline());
                return category;
            case FLAG:
                category.setFlag(countryRepository.findFlagByCountryId(countryId));
                return category;
            case LOCATION:
                Location location = countryRepository.findLocationByCountryId(countryId);
                category.setLocation(location);
                return category;

            case CAPITAL:
                category.setCapital(countryRepository.findCapitalByCountryId(countryId));
                return category;

            default:
                return null;
        }
    }


    public Game createGame(String username) {
        Game game = new Game();

        GameUser lobbyCreator = new GameUser();
        lobbyCreator.setUsername(username);

        Long initialCountryId= countryService.getAllCountryIdsWithRandomId();

        game.setCountriesToPlayIds(countryRepository.getAllCountryIds());
        game.setLobbyCreator(lobbyCreator);
        game.setCurrentState(GameState.SETUP);
        game.setCurrentCountryId(initialCountryId);

        CategoryStack categoryStack = new CategoryStack();
        categoryStack.addAll(Arrays.asList(CategoryEnum.values()));

        game.setCategoryStack(categoryStack);
        game.setRemainingTime(30L);
        gameRepository.saveAndFlush(game);
        return game;
    }


    private class GameUpdater implements Runnable {
        private final Long gameId;
        private final String topic;

        public GameUpdater(Long gameId, String topic) {
            this.gameId = gameId;
            this.topic = topic;
        }

        @Override
        public void run() {
            try {
                updateGameEverySecond(gameId);
            } catch (Exception e) {
                log.error("Error during game update execution", e);
            }
        }
    }

    public Game startGame(Long gameId) {
        Game game = gameRepository.findByGameId(gameId);
        game.setCurrentState(GameState.GUESSING);

        final Game game2 = gameRepository.saveAndFlush(game);

        WebsocketPackage websocketPackage3 = new WebsocketPackage();
        websocketPackage3.setType(WebsocketType.GAMESTATEUPDATE);
        websocketPackage3.setPayload(game.getCurrentState());
        messagingTemplate.convertAndSend("/topic/game/" + gameId, websocketPackage3);

        String topic = "/topic/game/" + game2.getGameId();
        GameUpdater gameUpdater = new GameUpdater(game2.getGameId(), topic);

        ScheduledFuture<?> scheduledFuture = scheduler.scheduleAtFixedRate(gameUpdater, 1, 1, TimeUnit.SECONDS);

        scheduledFutures.put(gameId, scheduledFuture);

        return game;
    }
    public void stopGame(Long gameId) {
    System.out.println("Stopping game");
        ScheduledFuture<?> scheduledFuture = scheduledFutures.get(gameId);
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
            scheduledFutures.remove(gameId);
        }
    }

    public void submitGuess(Long gameId, Guess guess) {
        try {
            System.out.println("The guess submitted is:" + guess.getGuess());
        Game game = gameRepository.findByGameId(gameId);
        if (countryRepository.findNameByCountryId(game.getCurrentCountryId()).equals(guess.getGuess())) {
            return;
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Your guess was wrong");
        }
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.toString());
            }

    }

    private void updateGameEverySecond(Long gameId) {
        System.out.println("Updating game every second");
        // get the latest game state from the GameRepository
        Game game = gameRepository.findByGameId(gameId);
        // decrement the time remaining by 1 second
        Long timeRemaining = game.getRemainingTime();
        if (timeRemaining > 0 && game.getCurrentState() == GameState.GUESSING){
            game.setRemainingTime(timeRemaining - 1);
            WebsocketPackage websocketPackage1 = new WebsocketPackage();
            websocketPackage1.setType(WebsocketType.TIMEUPDATE);
            websocketPackage1.setPayload(game.getRemainingTime());
            messagingTemplate.convertAndSend("/topic/game/" + gameId, websocketPackage1);

            if(timeRemaining % 5 == 0){
                CategoryStack remainingCategories = game.getCategoryStack();
                if(!remainingCategories.isEmpty()){
                    System.out.println("Remaining categories: " + remainingCategories);


                    CategoryEnum currentCategoryEnum = remainingCategories.pop();
                    remainingCategories.setCurrentCategory(currentCategoryEnum);
                    Category category = transformToCategory(currentCategoryEnum, game.getCurrentCountryId());
                    gameRepository.saveAndFlush(game);

                    WebsocketPackage websocketPackage = new WebsocketPackage();
                    websocketPackage.setType(WebsocketType.CATEGORYUPDATE);
                    websocketPackage.setPayload(category);
                    messagingTemplate.convertAndSend("/topic/game/" + gameId, websocketPackage);
                }
            }
            gameRepository.saveAndFlush(game);

        }else{
            game.setCurrentState(GameState.SCOREBOARD);
            gameRepository.saveAndFlush(game);
            stopGame(gameId);

            WebsocketPackage websocketPackage3 = new WebsocketPackage();
            websocketPackage3.setType(WebsocketType.GAMESTATEUPDATE);
            websocketPackage3.setPayload(game.getCurrentState());
            messagingTemplate.convertAndSend("/topic/game/" + gameId, websocketPackage3);
        }
    }

    public String getGameCountryName(Long gameId){
        Game game = gameRepository.findByGameId(gameId);
        return countryRepository.findNameByCountryId(game.getCurrentCountryId());
    }
    public Game getGameById(Long id){
        return gameRepository.findByGameId(id);
    }

    public void addSubscriber(Long gameId, String sessionId) {
        List<String> subscribers = subscribersByGameId.computeIfAbsent(gameId, k -> new CopyOnWriteArrayList<>());
        if (!subscribers.contains(sessionId)) {
            subscribers.add(sessionId);
        }
        Game game = gameRepository.findByGameId(gameId);
        System.out.println("New subscriber added");
    }

    public void removeSubscriber(Long gameId, String sessionId) {
        List<String> subscribers = subscribersByGameId.get(gameId);
        if (subscribers != null) {
            subscribers.remove(sessionId);
            if (subscribers.isEmpty()) {
                subscribersByGameId.remove(gameId);
            }
        }
    }

    public void sendGameUpdates(Long gameId, WebsocketPackage websocketPackage) {
        List<String> subscribers = subscribersByGameId.get(gameId);
        if (subscribers != null) {
            for (String sessionId : subscribers) {
                messagingTemplate.convertAndSendToUser(sessionId, "/topic/game/" + gameId, websocketPackage);
            }
        }
    }
}
