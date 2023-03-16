package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.GameState;
import ch.uzh.ifi.hase.soprafs23.entity.Country;
import ch.uzh.ifi.hase.soprafs23.entity.Game;
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

import java.util.List;
import java.util.Map;
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
    public GameService(@Qualifier("gameRepository")GameRepository gameRepository, SimpMessagingTemplate messagingTemplate, CountryService countryService) {
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

        Game game = new Game();
        game.setCreatorUsername(username);
        game.setGameState(GameState.SETUP);
        Country initialCountry = countryService.getRandomCountry();
        game.setCurrentCountry(initialCountry.getName());
        game.setCurrentPopulation(initialCountry.getPopulation().toString());
        game.setCurrentFlag(initialCountry.getFlag());
        game.setCurrentLatitude(initialCountry.getLatitude());
        game.setCurrentLongitude(initialCountry.getLongitude());
        game.setTime(60L);
        gameRepository.save(game);
        gameRepository.flush();
        return game;
    }

    public Game startGame(Long gameId) {
        Game game = gameRepository.findByid(gameId);
        game.setGameState(GameState.GUESSING);
        final Game game2 = gameRepository.saveAndFlush(game);
        String topic = "/topic/game/" + game2.getId();
        ScheduledFuture<?> scheduledFuture = scheduler.scheduleAtFixedRate(() -> {
            messagingTemplate.convertAndSend(topic, getGameUpdates(game2.getId()));
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
        Game game = gameRepository.findByid(gameId);
        // decrement the time remaining by 1 second
        Long timeRemaining = game.getTime();
        if (timeRemaining > 0) {
            Country randomCountry = countryService.getRandomCountry();
            game.setTime(timeRemaining - 1);
            if(timeRemaining % 5 == 0){
                game.setCurrentCountry(randomCountry.getName());
                game.setCurrentPopulation(randomCountry.getPopulation().toString());
                game.setCurrentFlag(randomCountry.getFlag());
                game.setCurrentLatitude(randomCountry.getLatitude());
                game.setCurrentLongitude(randomCountry.getLongitude());

            }
            gameRepository.saveAndFlush(game);

        }else{
            game.setGameState(GameState.SCOREBOARD);
            gameRepository.saveAndFlush(game);
            stopGame(gameId);

        }
        // create a GameGetDTO object with the latest game state
        GameGetDTO gameGetDTO = DTOMapper.INSTANCE.convertEntityToGameGetDTO(game);
        sendGameUpdates(gameId, gameGetDTO);
        return gameGetDTO;
    }

    public Game getGameById(Long id){
        return gameRepository.findByid(id);
    }

    public void addSubscriber(Long gameId, String sessionId) {
        List<String> subscribers = subscribersByGameId.computeIfAbsent(gameId, k -> new CopyOnWriteArrayList<>());
        if (!subscribers.contains(sessionId)) {
            subscribers.add(sessionId);
        }
        Game game = gameRepository.findByid(gameId);
        GameGetDTO gameGetDTO = DTOMapper.INSTANCE.convertEntityToGameGetDTO(game);
        sendGameUpdates(gameId, gameGetDTO);
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

    public void sendGameUpdates(Long gameId, GameGetDTO gameUpdates) {
        List<String> subscribers = subscribersByGameId.get(gameId);
        if (subscribers != null) {
            for (String sessionId : subscribers) {
                messagingTemplate.convertAndSendToUser(sessionId, "/topic/game/" + gameId, gameUpdates);
            }
        }
    }



}
