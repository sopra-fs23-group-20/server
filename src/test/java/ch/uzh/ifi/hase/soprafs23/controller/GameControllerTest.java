package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.constant.GameState;
import ch.uzh.ifi.hase.soprafs23.entityDB.Game;
import ch.uzh.ifi.hase.soprafs23.entityDB.GameUser;
import ch.uzh.ifi.hase.soprafs23.entityOther.Guess;
import ch.uzh.ifi.hase.soprafs23.rest.dto.GamePostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.GuessPostDTO;
import ch.uzh.ifi.hase.soprafs23.service.GameService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(GameController.class)

public class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    @Test
    public void getAllGames_noGamesFound_emptyListReturned() throws Exception {
        // given
        given(gameService.getGames()).willReturn(Collections.emptyList());

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder getRequest = MockMvcRequestBuilders.get("/games")
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    /*
    @Test
    public void getAllGames_gamesFound_gamesReturned() throws Exception {
        // given
        List<Game> games = new ArrayList<>();
        Game game1 = new Game();
        game1.setGameId(1L);
        games.add(game1);
        Game game2 = new Game();
        game2.setGameId(2L);
        games.add(game2);

        given(gameService.getGames()).willReturn(games);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder getRequest = MockMvcRequestBuilders.get("/games")
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].gameId", is(game1.getGameId().intValue())))
                .andExpect(jsonPath("$[1].gameId", is(game2.getGameId().intValue())));
    }



    @Test
    public void createGame_validInput_gameCreated() throws Exception {
        // given
        GamePostDTO gamePostDTO = new GamePostDTO();

        Game createdGame = new Game();
        createdGame.setGameId(1L);

        given(gameService.createGame(Mockito.any(GamePostDTO.class))).willReturn(createdGame);

        String gameJson = new ObjectMapper().writeValueAsString(gamePostDTO);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/games")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gameJson);

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.gameId", is(createdGame.getGameId().intValue())));

    }


    @Test
    public void updateGameConfiguration_validInput_gameUpdated() throws Exception {
        // given
        GamePutDTO gamePutDTO = new GamePutDTO();

        Game updatedGame = new Game();
        updatedGame.setGameId(1L);

        given(gameService.updateGameConfig(Mockito.any(GamePutDTO.class))).willReturn(updatedGame);

        String gameJson = new ObjectMapper().writeValueAsString(gamePutDTO);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/games/{gameId}", updatedGame.getGameId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(gameJson)
                .header("Authorization", "1");

        // then
        mockMvc.perform(putRequest)
                .andExpect(status().isNoContent());
    }

    @Test
    public void getGameInfo_validInput_gameFound() throws Exception {
        // given
        Game game = new Game();
        game.setGameId(1L);

        given(gameService.getGame(Mockito.anyLong())).willReturn(game);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder getRequest = MockMvcRequestBuilders.get("/games/{gameId}", game.getGameId())
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "1");

        // then
        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gameId", is(game.getGameId().intValue())));
    }*/


    @Test
    public void getGameInfo_validInput_gameNotFound() throws Exception {
        given(gameService.getGame(Mockito.anyLong())).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        MockHttpServletRequestBuilder getRequest = MockMvcRequestBuilders.get("/games/{gameId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "1");

        // then
        mockMvc.perform(getRequest)
                .andExpect(status().isNotFound());

    }


    @Test
    public void getGameCountry_validInput_countryFound() throws Exception {
        // given
        Long gameId = 1L;
        String country = "USA";

        given(gameService.getGameCountryName(gameId)).willReturn(country);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder getRequest = MockMvcRequestBuilders.get("/games/{gameId}/country", gameId)
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(content().string(country));
    }

    @Test
    public void getGameCountry_invalidInput_gameNotFound() throws Exception {
        // given
        Long gameId = 1L;

        given(gameService.getGameCountryName(gameId)).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder getRequest = MockMvcRequestBuilders.get("/games/{gameId}/country", gameId)
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest)
                .andExpect(status().isNotFound());
    }

    @Test
    public void getGameCountries_validInput_gameFound() throws Exception {
        // given
        Long gameId = 1L;
        List<String> countries = Arrays.asList("USA", "Canada", "Mexico");
        given(gameService.getGameCountriesNames(gameId)).willReturn(countries);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder getRequest = MockMvcRequestBuilders.get("/games/{gameId}/countries", gameId)
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(countries.size())))
                .andExpect(jsonPath("$[0]", is(countries.get(0))))
                .andExpect(jsonPath("$[1]", is(countries.get(1))))
                .andExpect(jsonPath("$[2]", is(countries.get(2))));
    }

    @Test
    public void getGameCountries_validInput_gameNotFound() throws Exception {
        // given
        Long gameId = 1L;
        given(gameService.getGameCountriesNames(gameId)).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder getRequest = MockMvcRequestBuilders.get("/games/{gameId}/countries", gameId)
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest)
                .andExpect(status().isNotFound());
    }

    @Test
    public void startGame_validInput_gameStarted() throws Exception {
        // given
        Game game = new Game();
        game.setGameId(1L);

        GameUser gameUser = new GameUser();
        gameUser.setUserId(2L);
        gameUser.setUsername("testUser");
        gameUser.setUserPlayingState(GameState.GUESSING);
        gameUser.setGamePoints(0L);

        Set<GameUser> participants = new HashSet<>();
        participants.add(gameUser);
        game.setParticipants(participants);

        game.setCurrentState(GameState.GUESSING);
        game.setRemainingRoundPoints(10L);
        game.setRemainingRounds(5L);
        game.setRoundDuration(30);
        game.setRandomizedHints(false);
        game.setNumberOfRounds(5L);
        game.setOpenLobby(false);


        given(gameService.startGame(Mockito.anyLong())).willReturn(game);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/games/{gameId}/start", game.getGameId())
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(putRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gameId", is(game.getGameId().intValue())))
                .andExpect(jsonPath("$.currentState", is(game.getCurrentState().toString())));
                //.andExpect(jsonPath("$.remainingRoundPoints", is(game.getRemainingRoundPoints())))
                //.andExpect(jsonPath("$.remainingRounds", is(game.getRemainingRounds())))
                //.andExpect(jsonPath("$.roundSeconds", is(game.getRoundSeconds())))
                //.andExpect(jsonPath("$.randomizedHints", is(game.getRandomizedHints())))
                //.andExpect(jsonPath("$.numberOfRounds", is(game.getNumberOfRounds())))
                //.andExpect(jsonPath("$.openLobby", is(game.getOpenLobby())));
    }


    @Test
    public void startGame_invalidInput_gameNotFound() throws Exception {
        given(gameService.startGame(Mockito.anyLong())).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        MockHttpServletRequestBuilder putRequest = put("/games/{gameId}/start", 1L)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(putRequest)
                .andExpect(status().isNotFound());
    }


    /*
    @Test
    public void joinGame_validInput_gameJoined() throws Exception {
        // given
        Game game = new Game();
        game.setGameId(1L);

        Set<GameUser> participants = new HashSet<>();
        GameUser gameUser = new GameUser();
        gameUser.setUserId(1L);
        participants.add(gameUser);
        game.setParticipants(participants);

        game.setCurrentState(GameState.SETUP);

        given(gameService.joinGame(Mockito.anyLong(), Mockito.anyLong())).willReturn(game);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/games/{gameId}/join", game.getGameId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("2");

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gameId", is(game.getGameId().intValue())))
                .andExpect(jsonPath("$.currentState", is(game.getCurrentState().toString())))
                .andExpect(jsonPath("$.participants[0].userId", is(game.getParticipants().iterator().next().getUserId().intValue())));
    }

     */

    @Test
    public void joinGame_invalidInput_gameNotFound() throws Exception {
        // given
        given(gameService.joinGame(Mockito.anyLong(), Mockito.anyLong())).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/games/{gameId}/join", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("2");

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isNotFound());
    }

    @Test
    public void joinGame_invalidInput_userAlreadyJoined() throws Exception {
        // given
        given(gameService.joinGame(Mockito.anyLong(), Mockito.anyLong())).willThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST));

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/games/{gameId}/join", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("2");

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void submitGuess_validGuess_guessSubmitted() throws Exception {
        // given
        Game game = new Game();
        game.setGameId(1L);

        GameUser gameUser = new GameUser();
        gameUser.setUserId(2L);
        gameUser.setUsername("testUser");
        gameUser.setUserPlayingState(GameState.GUESSING);
        gameUser.setGamePoints(0L);

        Set<GameUser> participants = new HashSet<>();
        participants.add(gameUser);
        game.setParticipants(participants);

        game.setCurrentState(GameState.GUESSING);
        game.setRemainingRoundPoints(10L);
        game.setRemainingRounds(5L);
        game.setNumberOfRounds(30L);
        game.setRandomizedHints(false);
        game.setNumberOfRounds(5L);
        game.setOpenLobby(false);


        GuessPostDTO guessPostDTO = new GuessPostDTO();
        guessPostDTO.setUserId(2L);
        guessPostDTO.setGuess("Germany");

        given(gameService.submitGuess(Mockito.anyLong(), Mockito.any(Guess.class))).willReturn("Your guess was right you get 10 points");

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/games/{gameId}/guesses", game.getGameId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(guessPostDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isCreated())
                .andExpect(content().string("Your guess was right you get 10 points"));
    }



    @Test
    public void submitGuess_invalidGuess_guessNotSubmitted() throws Exception {
        // given
        Game game = new Game();
        game.setGameId(1L);

        GameUser gameUser = new GameUser();
        gameUser.setUserId(2L);
        gameUser.setUsername("testUser");
        gameUser.setUserPlayingState(GameState.GUESSING);
        gameUser.setGamePoints(0L);

        Set<GameUser> participants = new HashSet<>();
        participants.add(gameUser);
        game.setParticipants(participants);
        game.setCurrentState(GameState.GUESSING);
        game.setRemainingRoundPoints(10L);
        game.setRemainingRounds(5L);
        game.setNumberOfRounds(30L);
        game.setRandomizedHints(false);
        game.setNumberOfRounds(5L);
        game.setOpenLobby(false);

        GuessPostDTO guessPostDTO = new GuessPostDTO();
        guessPostDTO.setUserId(2L);
        guessPostDTO.setGuess("USA");

        given(gameService.submitGuess(Mockito.anyLong(), Mockito.any(Guess.class))).willReturn("Your guess was wrong you get 0 points");

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/games/{gameId}/guesses", game.getGameId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(guessPostDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isCreated())
                .andExpect(content().string("Your guess was wrong you get 0 points"));
    }



    private String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        }
        catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("The request body could not be created.%s", e));
        }
    }
}

