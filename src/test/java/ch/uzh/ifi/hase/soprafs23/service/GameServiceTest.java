package ch.uzh.ifi.hase.soprafs23.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import ch.uzh.ifi.hase.soprafs23.constant.*;
import ch.uzh.ifi.hase.soprafs23.entityDB.*;
import ch.uzh.ifi.hase.soprafs23.repository.CountryRepository;
import ch.uzh.ifi.hase.soprafs23.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs23.rest.dto.GamePostDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.lang.reflect.Field;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ScheduledFuture;

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
    void testCreateGameSuccess() {
        GamePostDTO gamePostDTO = new GamePostDTO();
        gamePostDTO.setLobbyCreatorUserId("1");
        gamePostDTO.setRoundDuration(120L);
        gamePostDTO.setNumberOfRounds(5L);
        gamePostDTO.setOpenLobby(true);
        gamePostDTO.setSelectedRegions(Arrays.asList(RegionEnum.AFRICA, RegionEnum.EUROPE));
        gamePostDTO.setDifficulty(Difficulty.EASY);
        gamePostDTO.setCategoryStack(new CategoryStack());

        User user = new User();
        user.setUserId(1L);
        user.setCreation_date(new Date());
        user.setToken("1234");
        user.setStatus(UserStatus.ONLINE);

        Page<Country> page = new PageImpl<>(Collections.emptyList());
        when(countryRepository.getCountriesByRegionsAndDifficulty(anyList(), anyLong(), any(Pageable.class))).thenReturn(page);

        when(userRepository.findByUserId(1L)).thenReturn(user);

        Game result = gameService.createGame(gamePostDTO);

        assertNotNull(result);
        assertEquals(1L, result.getLobbyCreator().getUserId());
        assertEquals(2, result.getSelectedRegions().size());
        assertEquals(Difficulty.EASY, result.getDifficulty());
        assertTrue(result.getOpenLobby());
        assertNotNull(result.getCategoryStack());
        assertEquals(GameState.SETUP, result.getCurrentState());
        assertEquals(120L, result.getRoundDuration());
        assertEquals(5L, result.getNumberOfRounds());
    }

    @Test
    void testStopGame() throws Exception {
        // Create a mock gameId
        Long gameId = 1L;

        // Mock a ScheduledFuture
        ScheduledFuture<?> scheduledFutureMock = mock(ScheduledFuture.class);

        // Set the mock ScheduledFuture in the scheduledFutures map using reflection
        Field scheduledFuturesField = GameService.class.getDeclaredField("scheduledFutures");
        scheduledFuturesField.setAccessible(true);
        Map<Long, ScheduledFuture<?>> scheduledFutures = (Map<Long, ScheduledFuture<?>>) scheduledFuturesField.get(gameService);
        scheduledFutures.put(gameId, scheduledFutureMock);

        // Call the stopGame method
        gameService.stopGame(gameId);

        // Verify that cancel(false) is called on the mock ScheduledFuture
        verify(scheduledFutureMock).cancel(false);

        // Verify that the gameId is removed from the scheduledFutures map using reflection
        assertFalse(scheduledFutures.containsKey(gameId));
    }

    @Test
    void testCreateGameWithNullLobbyCreatorUser() {
        GamePostDTO gamePostDTO = new GamePostDTO();
        gamePostDTO.setLobbyCreatorUserId("1");
        gamePostDTO.setRoundDuration(120L);
        gamePostDTO.setNumberOfRounds(5L);
        gamePostDTO.setOpenLobby(true);
        gamePostDTO.setSelectedRegions(Arrays.asList(RegionEnum.AFRICA, RegionEnum.EUROPE));
        gamePostDTO.setDifficulty(Difficulty.EASY);
        gamePostDTO.setCategoryStack(new CategoryStack());

        when(userRepository.findByUserId(1L)).thenReturn(null);

        assertThrows(RuntimeException.class, () -> gameService.createGame(gamePostDTO));

        verify(gameRepository, times(0)).saveAndFlush(any(Game.class));
    }

    @Test
    void testCheckIfEveryoneGuessed_AllGuessedCorrectly() throws Exception {
        // Arrange
        Game game = new Game();
        Set<GameUser> participants = new HashSet<>();

        GameUser user1 = new GameUser();
        user1.setNumberOfGuessesLeft(0L);
        user1.setGuessedCorrectly(true);

        GameUser user2 = new GameUser();
        user2.setNumberOfGuessesLeft(0L);
        user2.setGuessedCorrectly(true);

        participants.add(user1);
        participants.add(user2);

        game.setParticipants(participants);

        // Act
        Method method = GameService.class.getDeclaredMethod("checkIfEveryoneGuessed", Game.class);
        method.setAccessible(true);
        boolean result = (boolean) method.invoke(gameService, game);

        // Assert
        assertTrue(result);
    }

    @Test
    void testCheckIfEveryoneGuessed_NotEveryoneGuessedCorrectly() throws Exception {
        // Arrange
        Game game = new Game();
        Set<GameUser> participants = new HashSet<>();

        GameUser user1 = new GameUser();
        user1.setNumberOfGuessesLeft(1L);
        user1.setGuessedCorrectly(false);

        GameUser user2 = new GameUser();
        user2.setNumberOfGuessesLeft(0L);
        user2.setGuessedCorrectly(true);

        participants.add(user1);
        participants.add(user2);

        game.setParticipants(participants);

        // Act
        Method method = GameService.class.getDeclaredMethod("checkIfEveryoneGuessed", Game.class);
        method.setAccessible(true);
        boolean result = (boolean) method.invoke(gameService, game);

        // Assert
        assertFalse(result);
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

        assertThrows(RuntimeException.class,
                () -> gameService.getCountryIdsByRegionsAndDifficulty(regions, difficulty));
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

    /*
    @Test
    void lastPlayerSubmitGuessGoToScoreboard() {
        Game game = new Game();

        GameUser participant1 = new GameUser();
        GameUser participant2 = new GameUser();
        GameUser participant3 = new GameUser();

        participant1.setNumberOfGuessesLeft(0L);
        participant2.setNumberOfGuessesLeft(1L);
        participant3.setNumberOfGuessesLeft(1L);

        participant1.setGamePoints(10L);
        participant2.setGamePoints(30L);
        participant3.setGamePoints(50L);

        participant1.setUserId(2L);
        participant2.setUserId(3L);
        participant3.setUserId(4L);

        game.setParticipants(new HashSet<>(Arrays.asList(participant1, participant2, participant3)));
        game.setCurrentState(GameState.GUESSING);
        game.setRemainingRoundPoints(30L);
        game.setRemainingRounds(1L);
        game.setRoundDuration(30L);
        game.setNumberOfGuesses(1L);


        when(countryRepository.findNameByCountryId(anyLong())).thenReturn("Switzerland");
        when(gameService.getActiveGame(anyLong())).thenReturn(game);

        Guess guess1 = new Guess();
        guess1.setGuess("Switzerland");
        guess1.setUserId(3L);

        assertNotEquals(game.getCurrentState(), GameState.SCOREBOARD);
        gameService.submitGuess(1L, guess1);
        assertNotEquals(game.getCurrentState(), GameState.SCOREBOARD);

        Guess guess2 = new Guess();
        guess2.setGuess("France");
        guess2.setUserId(4L);

        gameService.submitGuess(1L, guess2);
        assertEquals(game.getCurrentState(), GameState.SCOREBOARD);
    }

     */

}
