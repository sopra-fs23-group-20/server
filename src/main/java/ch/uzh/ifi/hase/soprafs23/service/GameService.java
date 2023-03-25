package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.CategoryEnum;
import ch.uzh.ifi.hase.soprafs23.constant.GameState;
import ch.uzh.ifi.hase.soprafs23.constant.WebsocketType;
import ch.uzh.ifi.hase.soprafs23.entity.*;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
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



    @Autowired
    public GameService(@Qualifier("gameRepository")GameRepository gameRepository, SimpMessagingTemplate messagingTemplate, CountryService countryService){
        this.gameRepository = gameRepository;
        this.messagingTemplate = messagingTemplate;
        this.countryService = countryService;
    }

    public List<Game> getGames(){
        return this.gameRepository.findAll();
    }

    public List<String> getGameCountries(Long gameId){
        Game game = gameRepository.findByGameId(gameId);
        Set<GameCountry> gameCountries = game.getCountriesToPlay();
        List<String> countryNames = new ArrayList<>();

        for(GameCountry gameCountry : gameCountries){
            countryNames.add(gameCountry.getName());
        }
        return countryNames;
    }


    public Game createGame(String username){
        if (countryService.getAllCountries().isEmpty()){
            countryService.setAllCountries();
        }


        GameUser lobbyCreator = new GameUser();
        lobbyCreator.setUsername(username);

        Country initialCountry = countryService.getRandomCountry();
        GameCountry inititalGameCountry = GameCountry.transformToGameCountry(initialCountry);

        Category currentCategory = Category.transformToCategory(CategoryEnum.POPULATION,inititalGameCountry);

        Game game = new Game();
        Set<GameCountry> countriesToPlay = GameCountry.addGameCountryCollection(countryService.getAllCountries());
        game.setCountriesToPlay(countriesToPlay);

        game.setLobbyCreator(lobbyCreator);
        game.setCurrentState(GameState.SETUP);

        game.setCurrentCountry(inititalGameCountry);
        game.setCurrentCategory(currentCategory);
        game.setRemainingTime(30L);
        gameRepository.save(game);
        gameRepository.flush();
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
        List<CategoryEnum> selectedCategories = new ArrayList<>();

        selectedCategories.add(CategoryEnum.CAPITAL);
        selectedCategories.add(CategoryEnum.FLAG);
        selectedCategories.add(CategoryEnum.LOCATION);
        selectedCategories.add(CategoryEnum.OUTLINE);
        selectedCategories.add(CategoryEnum.POPULATION);


        game.setCategoriesSelected(selectedCategories);

        CategoryStack remainingCategories = new CategoryStack();
        remainingCategories.addAll(selectedCategories);
        game.setRemainingCategories(remainingCategories);
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
        if (game.getCurrentCountry().getName().equals(guess.getGuess())) {
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
                CategoryStack remainingCategories = game.getRemainingCategories();
                if(!remainingCategories.isEmpty()){
                    System.out.println("Remaining categories: " + remainingCategories);


                    CategoryEnum currentCategoryEnum = remainingCategories.pop();
                    Category currentCategory = Category.transformToCategory(currentCategoryEnum, game.getCurrentCountry());

                    game.setCurrentCategory(currentCategory);
                    game.setRemainingCategories(remainingCategories);

                    gameRepository.saveAndFlush(game);

                    WebsocketPackage websocketPackage = new WebsocketPackage();
                    websocketPackage.setType(WebsocketType.CATEGORYUPDATE);
                    websocketPackage.setPayload(game.getCurrentCategory());
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
