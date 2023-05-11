package ch.uzh.ifi.hase.soprafs23.rest.mapper.GameTests;

import ch.uzh.ifi.hase.soprafs23.constant.Difficulty;
import ch.uzh.ifi.hase.soprafs23.constant.GameMode;
import ch.uzh.ifi.hase.soprafs23.constant.RegionEnum;
import ch.uzh.ifi.hase.soprafs23.entityDB.CategoryStack;
import ch.uzh.ifi.hase.soprafs23.rest.dto.GamePutDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GamePutDTOTest {

    private GamePutDTO gamePutDTO;

    @BeforeEach
    void setUp() {
        gamePutDTO = new GamePutDTO();
    }

    @Test
    void testUserId() {
        gamePutDTO.setUserId(1L);
        assertEquals(1L, gamePutDTO.getUserId());
    }

    @Test
    void testLobbyCreatorUserId() {
        gamePutDTO.setLobbyCreatorUserId("1");
        assertEquals("1", gamePutDTO.getLobbyCreatorUserId());
    }

    @Test
    void testRoundDuration() {
        gamePutDTO.setRoundDuration(60L);
        assertEquals(60L, gamePutDTO.getRoundDuration());
    }

    @Test
    void testNumberOfRounds() {
        gamePutDTO.setNumberOfRounds(10L);
        assertEquals(10L, gamePutDTO.getNumberOfRounds());
    }

    @Test
    void testCategoryStack() {
        CategoryStack categoryStack = new CategoryStack();
        gamePutDTO.setCategoryStack(categoryStack);
        assertEquals(categoryStack, gamePutDTO.getCategoryStack());
    }

    @Test
    void testSelectedRegions() {
        List<RegionEnum> selectedRegions = Arrays.asList(RegionEnum.AFRICA, RegionEnum.ASIA);
        gamePutDTO.setSelectedRegions(selectedRegions);
        assertEquals(selectedRegions, gamePutDTO.getSelectedRegions());
    }

    @Test
    void testOpenLobby() {
        gamePutDTO.setOpenLobby(true);
        assertTrue(gamePutDTO.isOpenLobby());
    }

    @Test
    void testDifficulty() {
        gamePutDTO.setDifficulty(Difficulty.EASY);
        assertEquals(Difficulty.EASY, gamePutDTO.getDifficulty());
    }

    @Test
    void testGameMode() {
        gamePutDTO.setGameMode(GameMode.NORMAL);
        assertEquals(GameMode.NORMAL, gamePutDTO.getGameMode());
    }
}
