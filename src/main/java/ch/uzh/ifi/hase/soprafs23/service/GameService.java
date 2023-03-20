package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.CategoryEnum;
import ch.uzh.ifi.hase.soprafs23.constant.GameState;
import ch.uzh.ifi.hase.soprafs23.constant.WebsocketType;
import ch.uzh.ifi.hase.soprafs23.entity.*;
import ch.uzh.ifi.hase.soprafs23.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs23.rest.dto.GameGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
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

    public Game createGame(String username){
        if (countryService.getAllCountries().isEmpty()){
            countryService.setCountriesWithFile();
        }

        GameUser lobbyCreator = new GameUser();
        lobbyCreator.setUsername(username);

        Country initialCountry = countryService.getRandomCountry();


        Category currentCategory = Category.transformToCategory(CategoryEnum.POPULATION,initialCountry);

        Game game = new Game();
        game.setLobbyCreator(lobbyCreator);
        game.setCurrentState(GameState.SETUP);

        game.setCurrentCountry(initialCountry);
        game.setCurrentCategory(currentCategory);
        game.setRemainingTime(60L);
        gameRepository.save(game);
        gameRepository.flush();
        return game;
    }

    public Game startGame(Long gameId) {
        Game game = gameRepository.findByGameId(gameId);
        game.setCurrentState(GameState.GUESSING);
        List<CategoryEnum> selectedCategories = new ArrayList<>();
        selectedCategories.add(CategoryEnum.POPULATION);
        selectedCategories.add(CategoryEnum.FLAG);
        selectedCategories.add(CategoryEnum.CAPITAL);
        selectedCategories.add(CategoryEnum.LOCATION);
        game.setCategoriesSelected(selectedCategories);

        CategoryStack remainingCategories = new CategoryStack();
        remainingCategories.addAll(selectedCategories);
        game.setRemainingCategories(remainingCategories);

        final Game game2 = gameRepository.saveAndFlush(game);
        String topic = "/topic/game/" + game2.getGameId();
        Websocket websocket = new Websocket();
        websocket.setType(WebsocketType.GAMESTATEUPDATE);
        websocket.setPayload(game.getCurrentState());
        sendGameUpdates(gameId, websocket);
        ScheduledFuture<?> scheduledFuture = scheduler.scheduleAtFixedRate(() -> {
            messagingTemplate.convertAndSend(topic, getGameUpdates(game2.getGameId()));
        }, 0, 1, TimeUnit.SECONDS);
        scheduledFutures.put(gameId, scheduledFuture);
        return game;
    }
    public void stopGame(Long gameId) {
        ScheduledFuture<?> scheduledFuture = scheduledFutures.get(gameId);
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
            scheduledFutures.remove(gameId);
        }
    }



    private GameGetDTO getGameUpdates(Long gameId) {
        // get the latest game state from the GameRepository
        Game game = gameRepository.findByGameId(gameId);
        // decrement the time remaining by 1 second
        Long timeRemaining = game.getRemainingTime();
        if (timeRemaining > 0) {
            game.setRemainingTime(timeRemaining - 1);
            if(timeRemaining % 5 == 0){
                CategoryStack remainingCategories = game.getRemainingCategories();
                CategoryEnum currentCategoryEnum = remainingCategories.pop();
                game.setRemainingCategories(remainingCategories);
                Category currentCategory = Category.transformToCategory(currentCategoryEnum, game.getCurrentCountry());
                game.setCurrentCategory(currentCategory);

            }
            gameRepository.saveAndFlush(game);

        }else{
            game.setCurrentState(GameState.SCOREBOARD);
            gameRepository.saveAndFlush(game);
            stopGame(gameId);

        }
        // create a GameGetDTO object with the latest game state
        GameGetDTO gameGetDTO = DTOMapper.INSTANCE.convertEntityToGameGetDTO(game);
        Websocket websocket = new Websocket();
        websocket.setType(WebsocketType.CATEGORYUPDATE);
        websocket.setPayload(game.getCurrentCategory());
        sendGameUpdates(gameId, websocket);
        return gameGetDTO;
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
        GameGetDTO gameGetDTO = DTOMapper.INSTANCE.convertEntityToGameGetDTO(game);
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

    public void sendGameUpdates(Long gameId, Websocket websocket) {
        List<String> subscribers = subscribersByGameId.get(gameId);
        if (subscribers != null) {
            for (String sessionId : subscribers) {
                messagingTemplate.convertAndSendToUser(sessionId, "/topic/game/" + gameId, websocket);
            }
        }
    }



}
