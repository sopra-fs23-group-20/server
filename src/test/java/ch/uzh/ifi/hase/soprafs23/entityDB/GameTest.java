package ch.uzh.ifi.hase.soprafs23.entityDB;

import ch.uzh.ifi.hase.soprafs23.StatePattern.*;
import ch.uzh.ifi.hase.soprafs23.constant.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private Game game;

    @BeforeEach
    void setUp() {
        game = new Game();
    }

    @Test
    void testGameId() {
        game.setGameId(1L);
        assertNotNull(game.getGameId());
        assertEquals(1L, game.getGameId());
    }

    @Test
    void testLobbyCreator() {
        GameUser gameUser = new GameUser();
        game.setLobbyCreator(gameUser);
        assertNotNull(game.getLobbyCreator());
        assertEquals(gameUser, game.getLobbyCreator());
    }

    @Test
    void testCreationDate() {
        Date date = new Date();
        game.setCreationDate(date);
        assertNotNull(game.getCreationDate());
        assertEquals(date, game.getCreationDate());
    }

    @Test
    void testCategoryStack() {
        CategoryStack categoryStack = new CategoryStack();
        categoryStack.add(CategoryEnum.POPULATION);
        game.setCategoryStack(categoryStack);
        assertNotNull(game.getCategoryStack());
        assertEquals(categoryStack, game.getCategoryStack());
    }

    @Test
    void testGameState() {

        Set<GameUser> users = new HashSet<>();
        users.add(new GameUser());

        game.setParticipants(users);
        game.setCurrentState(GameState.SETUP);
        assertNotNull(game.getCurrentState());
        assertEquals(GameState.SETUP, game.getCurrentState());
    }

    @Test
    void testRoundDuration () {
        game.setRoundDuration(60);
        assertNotNull(game.getRoundDuration());
        assertEquals(60, game.getRoundDuration());
    }

    @Test
    void testRemainingTime() {
        game.setRemainingTime(60L);
        assertNotNull(game.getRemainingTime());
        assertEquals(60L, game.getRemainingTime());
    }

    @Test
    void testNumberOfRounds() {
        game.setNumberOfRounds(30L);
        assertNotNull(game.getNumberOfRounds());
        assertEquals(30L, game.getNumberOfRounds());

    }

    @Test
    void testRemainingRounds() {
        game.setRemainingRounds(30L);
        assertNotNull(game.getRemainingRounds());
        assertEquals(30L, game.getRemainingRounds());

    }

    @Test
    void testRemainingRoundPoints() {
        game.setRemainingRoundPoints(30L);
        assertNotNull(game.getRemainingRoundPoints());
        assertEquals(30L, game.getRemainingRoundPoints());

    }

    @Test
    void testOpenLobby() {
        game.setOpenLobby(true);
        assertNotNull(game.getOpenLobby());
        assertEquals(true, game.getOpenLobby());
    }

    @Test
    void testDifficulty() {
        game.setDifficulty(Difficulty.EASY);
        assertNotNull(game.getDifficulty());
        assertEquals(Difficulty.EASY, game.getDifficulty());
    }

    @Test
    void TestCountryToPlayIds() {
        Set<Long> countryToPlayIds = new HashSet<>();
        countryToPlayIds.add(1L);
        countryToPlayIds.add(2L);
        game.setCountriesToPlayIds(countryToPlayIds);
        assertNotNull(game.getCountriesToPlayIds());
        assertEquals(countryToPlayIds, game.getCountriesToPlayIds());
    }

    @Test
    void testCurrentCountryId() {
        game.setCurrentCountryId(1L);
        assertNotNull(game.getCurrentCountryId());
        assertEquals(1L, game.getCurrentCountryId());
    }


    @Test
    void testAvailableHints() {
        List<CategoryEnum> hints = new ArrayList<>();
        hints.add(CategoryEnum.POPULATION);
        hints.add(CategoryEnum.OUTLINE);
        game.setAvailableHints(hints);
        assertNotNull(game.getAvailableHints());
        assertEquals(hints, game.getAvailableHints());
    }

    @Test
    void testSelectedRegions() {
        List<RegionEnum> regions = new ArrayList<>();
        regions.add(RegionEnum.AFRICA);
        regions.add(RegionEnum.ASIA);
        game.setSelectedRegions(regions);
        assertNotNull(game.getSelectedRegions());
        assertEquals(regions, game.getSelectedRegions());
    }

    @Test
    void testGetGameStateClass() {
        GameStateClass setupStateClass = Game.getGameStateClass(GameState.SETUP);
        assertTrue(setupStateClass instanceof SetupStateClass);

        GameStateClass guessingStateClass = Game.getGameStateClass(GameState.GUESSING);
        assertTrue(guessingStateClass instanceof GuessingStateClass);

        GameStateClass scoreboardStateClass = Game.getGameStateClass(GameState.SCOREBOARD);
        assertTrue(scoreboardStateClass instanceof ScoreboardStateClass);

        GameStateClass endedStateClass = Game.getGameStateClass(GameState.ENDED);
        assertTrue(endedStateClass instanceof EndedStateClass);
    }
}
