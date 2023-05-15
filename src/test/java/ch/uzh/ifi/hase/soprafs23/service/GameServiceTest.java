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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

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

    @InjectMocks
    private GameService gameService;

    @InjectMocks
    private CountryService countryService;


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
        when(countryService.getMinPopulationByDifficulty(any(Difficulty.class))).thenReturn(100L);

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

    @Test
    void testCreateGameWithInvalidRoundDuration() {
        GamePostDTO gamePostDTO = new GamePostDTO();
        gamePostDTO.setLobbyCreatorUserId("1");
        gamePostDTO.setRoundDuration(-1L);
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

        when(userRepository.findByUserId(1L)).thenReturn(user);

        verify(gameRepository, times(0)).saveAndFlush(any(Game.class));
    }

    @Test
    void testStartGameSuccess() throws Exception {
        Long gameId = 10001L;

        Set<GameUser> participants = new HashSet<>();
        participants.add(new GameUser());
        participants.add(new GameUser());

        Long country1 = 1L;
        Long country2 = 2L;
        Set<Long> countries = new HashSet<>();
        countries.add(country1);
        countries.add(country2);

        CategoryStack categoryStack = new CategoryStack();
        categoryStack.add(CategoryEnum.CAPITAL);

        Game game = new Game();
        game.setGameId(gameId);
        game.setParticipants(participants);
        game.setCurrentState(GameState.SETUP);
        game.setNumberOfRounds(10L);
        game.setCountriesToPlayIds(countries);
        game.setCategoryStack(categoryStack);
        game.setRoundDuration(30L);

        when(gameRepository.findByGameId(gameId)).thenReturn(game);
        when(gameRepository.saveAndFlush(game)).thenReturn(game);

        Field activeGamesField = GameService.class.getDeclaredField("activeGames");
        activeGamesField.setAccessible(true);
        Map<Long, Game> activeGames = (Map<Long, Game>) activeGamesField.get(gameService);

        activeGames.put(gameId, game);

        Game startedGame = gameService.startGame(gameId);

        assertNotNull(startedGame);
        assertEquals(GameState.GUESSING, startedGame.getCurrentState());
    }




    @Test
    void testStartGame_GameNotFound() {
        Long gameId = 10001L;

        assertThrows(RuntimeException.class, () -> gameService.startGame(gameId));
    }


    @Test
    void testStopGameNoScheduledFuture() {
        Long gameId = 1L;

        assertDoesNotThrow(() -> gameService.stopGame(gameId));
    }

    @Test
    void testGetCountryIdsByRegionsAndDifficultyNoCountries() {
        List<RegionEnum> regions = new ArrayList<>();
        regions.add(RegionEnum.EUROPE);
        regions.add(RegionEnum.ASIA);
        Difficulty difficulty = Difficulty.HARD;

        when(countryRepository.getCountriesByRegionsAndDifficulty(anyList(), any(), any())).thenReturn(Page.empty());
        when(countryService.getMinPopulationByDifficulty(any(Difficulty.class))).thenReturn(100L);

        Set<Long> countryIds = gameService.getCountryIdsByRegionsAndDifficulty(regions, difficulty);

        assertNotNull(countryIds);
        assertTrue(countryIds.isEmpty());

        verify(countryRepository, times(1)).getCountriesByRegionsAndDifficulty(eq(regions), anyLong(), any());
    }


    @Test
    void testGetAllGamesNoGames() {
        when(gameRepository.findAll()).thenReturn(new ArrayList<>());

        List<Game> allGames = gameService.getGames();

        assertNotNull(allGames);
        assertTrue(allGames.isEmpty());
    }

    @Test
    void testCheckIfEveryoneGuessedNoParticipants() throws Exception {
        Game game = new Game();
        game.setParticipants(new HashSet<>());

        Method method = GameService.class.getDeclaredMethod("checkIfEveryoneGuessed", Game.class);
        method.setAccessible(true);
        boolean result = (boolean) method.invoke(gameService, game);

        assertTrue(result);
    }






}
