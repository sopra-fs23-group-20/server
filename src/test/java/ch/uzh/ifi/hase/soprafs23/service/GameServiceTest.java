package ch.uzh.ifi.hase.soprafs23.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import ch.uzh.ifi.hase.soprafs23.entityDB.Game;
import ch.uzh.ifi.hase.soprafs23.entityDB.GameUser;
import ch.uzh.ifi.hase.soprafs23.entityDB.User;
import ch.uzh.ifi.hase.soprafs23.entityOther.Guess;
import ch.uzh.ifi.hase.soprafs23.repository.CountryRepository;
import ch.uzh.ifi.hase.soprafs23.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs23.constant.GameState;
import ch.uzh.ifi.hase.soprafs23.rest.dto.GamePostDTO;
import ch.uzh.ifi.hase.soprafs23.service.CountryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


class GameServiceTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CountryService countryService;

    @MockBean
    private SimpMessagingTemplate simpMessagingTemplate;

    @InjectMocks
    private GameService gameService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateGameSuccess() {
        GamePostDTO gamePostDTO = new GamePostDTO();
        gamePostDTO.setLobbyCreatorUserId("1");
        gamePostDTO.setRoundDuration(120L);
        gamePostDTO.setNumberOfRounds(5L);
        gamePostDTO.setRandomizedCategories(true);
        gamePostDTO.setOpenLobby(true);

        User testUser = new User();
        testUser.setUserId(1L);
        testUser.setUsername("TestUser");

        when(userRepository.findByUserId(1L)).thenReturn(testUser);
        when(gameRepository.saveAndFlush(any(Game.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Game createdGame = gameService.createGame(gamePostDTO);

        assertNotNull(createdGame);
        assertNotNull(createdGame.getGameId());
        assertEquals(1, createdGame.getParticipants().size());
        assertEquals(120, createdGame.getRoundDuration());
        assertEquals(5, createdGame.getNumberOfRounds());
        assertEquals(true, createdGame.getRandomizedHints());
        assertEquals(true, createdGame.getOpenLobby());

        verify(gameRepository, times(1)).saveAndFlush(any(Game.class));
    }

    @Test
    void testCreateGameFailure() {
        GamePostDTO gamePostDTO = new GamePostDTO();
        gamePostDTO.setLobbyCreatorUserId("1");
        gamePostDTO.setRoundDuration(120L);
        gamePostDTO.setNumberOfRounds(5L);
        gamePostDTO.setRandomizedCategories(true);
        gamePostDTO.setOpenLobby(true);

        when(userRepository.findByUserId(1L)).thenReturn(null);

        assertThrows(RuntimeException.class, () -> gameService.createGame(gamePostDTO));

        verify(gameRepository, times(0)).saveAndFlush(any(Game.class));
    }

    @Test
    void testJoinGameSuccess() {
        Long gameId = 10001L;
        Long userId = 2L;

        Game game = new Game();
        game.setGameId(gameId);
        game.setParticipants(new HashSet<>());

        User user = new User();
        user.setUserId(userId);
        user.setUsername("TestUser2");

        when(gameRepository.findByGameId(gameId)).thenReturn(game);
        when(userRepository.findByUserId(userId)).thenReturn(user);
        when(gameRepository.saveAndFlush(any(Game.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Game updatedGame = gameService.joinGame(gameId, userId);

        assertNotNull(updatedGame);
        assertEquals(1, updatedGame.getParticipants().size());

        verify(gameRepository, times(1)).saveAndFlush(any(Game.class));
    }

    @Test
    void testJoinGameFailure() {
        Long gameId = 10001L;
        Long userId = 2L;

        Game game = new Game();
        game.setGameId(gameId);
        game.setParticipants(new HashSet<>());

        User user = new User();
        user.setUserId(userId);
        user.setUsername("TestUser2");

        game.getParticipants().add(GameUser.transformUserToGameUser(user, game));

        when(gameRepository.findByGameId(gameId)).thenReturn(game);
        when(userRepository.findByUserId(userId)).thenReturn(user);

        assertThrows(ResponseStatusException.class, () -> gameService.joinGame(gameId, userId));

        verify(gameRepository, times(0)).saveAndFlush(any(Game.class));
    }

    /*
    @Test
    void testStartGameSuccess() {
        Long gameId = 10001L;

        Game game = new Game();
        game.setGameId(gameId);
        game.setParticipants(new HashSet<>());
        game.setNumberOfRounds(5L);

        when(gameRepository.findByGameId(gameId)).thenReturn(game);
        when(gameRepository.saveAndFlush(any(Game.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Game startedGame = gameService.startGame(gameId);

        assertNotNull(startedGame);
        assertEquals(GameState.SETUP, startedGame.getCurrentState());
        assertEquals(5, startedGame.getNumberOfRounds());

        verify(gameRepository, times(1)).saveAndFlush(any(Game.class));
    }*/

    @Test
    void testStartGameFailure() {
        Long gameId = 10001L;

        when(gameRepository.findByGameId(gameId)).thenReturn(null);

        assertThrows(RuntimeException.class, () -> gameService.startGame(gameId));

        verify(gameRepository, times(0)).saveAndFlush(any(Game.class));
    }

    /*@Test
    void testSubmitGuessSuccess() {
        Long gameId = 10001L;
        Long userId = 1L;
        Guess guess = new Guess();

        User user = new User();
        user.setUserId(userId);
        user.setUsername("TestUser");

        Game game = new Game();
        game.setGameId(gameId);
        game.setCurrentState(GameState.SETUP);
        game.setParticipants(new HashSet<>());

        GameUser gameUser = GameUser.transformUserToGameUser(user, game);
        game.getParticipants().add(gameUser);

        when(gameRepository.findByGameId(gameId)).thenReturn(game);
        when(userRepository.findByUserId(userId)).thenReturn(user);
        when(gameRepository.saveAndFlush(any(Game.class))).thenAnswer(invocation -> invocation.getArgument(0));

        assertDoesNotThrow(() -> gameService.submitGuess(gameId, guess));
        assertEquals(GameState.GUESSING, game.getCurrentState());
    }

    @Test
    void testSubmitGuessFailure() {
        Long gameId = 10001L;
        Long userId = 1L;
        Guess guess = new Guess();

        Game game = new Game();
        game.setGameId(gameId);
        game.setCurrentState(GameState.SETUP);
        game.setParticipants(new HashSet<>());

        User user = new User();
        user.setUserId(userId);
        user.setUsername("TestUser");

        game.getParticipants().add(GameUser.transformUserToGameUser(user, game));

        when(gameRepository.findByGameId(gameId)).thenReturn(game);
        when(userRepository.findByUserId(userId)).thenReturn(user);

        assertThrows(RuntimeException.class, () -> gameService.submitGuess(gameId, guess));

    }
    */








}
