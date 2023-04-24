package ch.uzh.ifi.hase.soprafs23.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import ch.uzh.ifi.hase.soprafs23.constant.Difficulty;
import ch.uzh.ifi.hase.soprafs23.constant.RegionEnum;
import ch.uzh.ifi.hase.soprafs23.entityDB.Country;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;


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
    void testCreateGameFailure() {
        GamePostDTO gamePostDTO = new GamePostDTO();
        gamePostDTO.setLobbyCreatorUserId("1");
        gamePostDTO.setRoundDuration(120L);
        gamePostDTO.setNumberOfRounds(5L);
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


    @Test
    void testStartGameFailure() {
        Long gameId = 10001L;

        when(gameRepository.findByGameId(gameId)).thenReturn(null);

        assertThrows(RuntimeException.class, () -> gameService.startGame(gameId));

        verify(gameRepository, times(0)).saveAndFlush(any(Game.class));
    }

    @Test
    void testStartGameAlreadyRunningFailure() {
        Long gameId = 10001L;

        Set<GameUser> participants = new HashSet<>();
        participants.add(new GameUser());
        participants.add(new GameUser());

        Game game = new Game();
        game.setGameId(gameId);
        game.setParticipants(participants);
        game.setCurrentState(GameState.GUESSING);

        when(gameRepository.findByGameId(gameId)).thenReturn(game);

        assertThrows(RuntimeException.class, () -> gameService.startGame(gameId));

        verify(gameRepository, times(0)).saveAndFlush(any(Game.class));
    }


    @Test
    void testGetCountryIdsByRegionsAndDifficultySuccess() {
        List<RegionEnum> regions = new ArrayList<>();
        regions.add(RegionEnum.EUROPE);
        regions.add(RegionEnum.ASIA);
        Difficulty difficulty = Difficulty.HARD;

        List<Country> countries = new ArrayList<>();
        countries.add(new Country());

        Page<Country> countryPage = new PageImpl<>(countries);

        when(countryRepository.getCountriesByRegionsAndDifficulty(anyList(), any(), any())).thenReturn(countryPage);

        Set<Long> countryIds = gameService.getCountryIdsByRegionsAndDifficulty(regions, difficulty);

        assertNotNull(countryIds);
        assertEquals(1, countryIds.size());
    }


    @Test
    void testGetCountryIdsByRegionsAndDifficultyFailure() {
        List<RegionEnum> regions = new ArrayList<>();
        regions.add(RegionEnum.EUROPE);
        regions.add(RegionEnum.ASIA);
        Difficulty difficulty = Difficulty.HARD;

        assertThrows(RuntimeException.class, () -> gameService.getCountryIdsByRegionsAndDifficulty(regions, difficulty));
    }

    @Test
    void testGetAllGamesSuccess() {
        List<Game> games = Arrays.asList(new Game(), new Game(), new Game());

        when(gameRepository.findAll()).thenReturn(games);

        List<Game> allGames = gameService.getGames();

        assertNotNull(allGames);
        assertEquals(3, allGames.size());
    }

    @Test
    void testGetGameByIdSuccess() {
        Long gameId = 10003L;

        Game game = new Game();
        game.setGameId(gameId);

        when(gameRepository.findByGameId(gameId)).thenReturn(game);

        Game foundGame = gameService.getGame(gameId);

        assertNotNull(foundGame);
        assertEquals(gameId, foundGame.getGameId());
    }

    @Test
    void testGetGameByIdFailure() {
        Long gameId = 10003L;

        when(gameRepository.findByGameId(gameId)).thenReturn(null);

        assertThrows(RuntimeException.class, () -> gameService.getGame(gameId));
    }


}
