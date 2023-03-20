package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.Guess;
import ch.uzh.ifi.hase.soprafs23.rest.dto.GameGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.GuessPostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@RestController
public class GameController {

    private final GameService gameService;

    private final SimpMessagingTemplate messagingTemplate;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);


    GameController(GameService gameService, SimpMessagingTemplate messagingTemplate) {
        this.gameService = gameService;
        this.messagingTemplate = messagingTemplate;
    }

    @GetMapping("/games")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<GameGetDTO> getAllGames() {
        // fetch all games in the internal representation
        List<Game> games = gameService.getGames();
        List<GameGetDTO> gameGetDTOs = new ArrayList<>();

        // convert each game to the API representation
        for (Game game : games) {
            gameGetDTOs.add(DTOMapper.INSTANCE.convertEntityToGameGetDTO(game));
        }
        return gameGetDTOs;
    }

    @PostMapping("/games")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public GameGetDTO createGame(@RequestBody String username) {
        // create game
        Game createdGame = gameService.createGame(username);
        // convert internal representation of game back to API
        return DTOMapper.INSTANCE.convertEntityToGameGetDTO(createdGame);
    }



    @GetMapping("/games/{gameId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameGetDTO getGameInfo(@PathVariable Long gameId) {
       Game game = gameService.getGameById(gameId);
         return DTOMapper.INSTANCE.convertEntityToGameGetDTO(game);
    }

    @GetMapping("/games/{gameId}/countries")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<String> getGameCountries(@PathVariable Long gameId) {
        return gameService.getGameCountries(gameId);
    }

    @PutMapping("/games/{gameId}/start")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameGetDTO startGame(@PathVariable Long gameId) {
        Game game = gameService.startGame(gameId);
        return DTOMapper.INSTANCE.convertEntityToGameGetDTO(game);
    }

    @PostMapping("/games/{gameId}/guesses")
    @ResponseStatus(HttpStatus.CREATED)
    public void submitGuess(@PathVariable Long gameId, @RequestBody GuessPostDTO guessPostDTO) {
        Guess guess = DTOMapper.INSTANCE.convertGuessPostDTOtoEntity(guessPostDTO);
        gameService.submitGuess(gameId, guess);
    }

    @MessageMapping("/game/{gameId}/join")
    public void joinGame(@DestinationVariable Long gameId, @Payload String sessionId) {
        // add the sessionId to the list of subscribers for the given gameId
        gameService.addSubscriber(gameId, sessionId);
        Game game = gameService.getGameById(gameId);
        messagingTemplate.convertAndSendToUser(sessionId, "/topic/game/" + gameId,DTOMapper.INSTANCE.convertEntityToGameGetDTO(game));
    }

    @MessageMapping("/game/{gameId}/leave")
    public void leaveGame(@DestinationVariable Long gameId, @Payload String sessionId) {
        // remove the sessionId from the list of subscribers for the given gameId
        gameService.removeSubscriber(gameId, sessionId);
    }







}
