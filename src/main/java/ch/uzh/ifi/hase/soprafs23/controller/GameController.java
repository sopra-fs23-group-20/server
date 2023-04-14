package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.entityDB.Game;
import ch.uzh.ifi.hase.soprafs23.entityOther.Guess;
import ch.uzh.ifi.hase.soprafs23.rest.dto.GamePostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.GameGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.GuessPostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class GameController {

    private final GameService gameService;


    GameController(GameService gameService) {
        this.gameService = gameService;

    }

    @GetMapping("/games")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<GameGetDTO> getAllGames() {
        // fetch all games in the internal representation which are public thus openlobby is TRUE
        List<Game> games = gameService.getOpenLobbyGames();
        List<GameGetDTO> gameGetDTOs = new ArrayList<>();

        // convert each game to the API representation
        for (Game game : games) {
            gameGetDTOs.add(DTOMapper.INSTANCE.convertEntityToGameGetDTO(game));
        }
        return gameGetDTOs;
    }

    @GetMapping("/allGames")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<GameGetDTO> getAllPublicGames() {
        // fetch all games in the internal representation incl. the private Game lobbies
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
    public GameGetDTO createGame(@RequestBody GamePostDTO gamePostDTO) {
        Game createdGame = gameService.createGame(gamePostDTO);
        // convert internal representation of game back to API
        return DTOMapper.INSTANCE.convertEntityToGameGetDTO(createdGame);
    }


    @GetMapping("/games/{gameId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameGetDTO getGameInfo(@PathVariable Long gameId) {
        Game game = gameService.getGame(gameId);
        return DTOMapper.INSTANCE.convertEntityToGameGetDTO(game);
    }

    @GetMapping("/games/{gameId}/country")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String getGameCountry(@PathVariable Long gameId) {
        return gameService.getGameCountryName(gameId);
    }

    @GetMapping("/games/{gameId}/countries")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<String> getGameCountries(@PathVariable Long gameId) {
        return gameService.getGameCountriesNames(gameId);
    }

    @PutMapping("/games/{gameId}/start")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameGetDTO startGame(@PathVariable Long gameId) {
        Game game = gameService.startGame(gameId);
        return DTOMapper.INSTANCE.convertEntityToGameGetDTO(game);
    }

    @PostMapping("/games/{gameId}/join")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameGetDTO joinGame(@PathVariable Long gameId, @RequestBody String userId) {
        Game game = gameService.joinGame(gameId, Long.parseLong(userId));
        return DTOMapper.INSTANCE.convertEntityToGameGetDTO(game);
    }

    @PostMapping("/games/{gameId}/guesses")
    @ResponseStatus(HttpStatus.CREATED)
    public String submitGuess(@PathVariable Long gameId, @RequestBody GuessPostDTO guessPostDTO) {
        Guess guess = DTOMapper.INSTANCE.convertGuessPostDTOtoEntity(guessPostDTO);
        return gameService.submitGuess(gameId, guess);
    }

    /**
     @GetMapping("/games/{gameId}/scoreboard")
     @ResponseStatus(HttpStatus.OK) public x getScoreboard(@PathVariable Long gameId) {
     //TODO work out how the scoreboard is stored and returned
     return GameService.getScoreboard(gameId);
     }
     */
}

