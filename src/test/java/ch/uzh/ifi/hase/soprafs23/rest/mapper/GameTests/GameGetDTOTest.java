package ch.uzh.ifi.hase.soprafs23.rest.mapper.GameTests;

import ch.uzh.ifi.hase.soprafs23.constant.CategoryEnum;
import ch.uzh.ifi.hase.soprafs23.constant.GameState;
import ch.uzh.ifi.hase.soprafs23.constant.RegionEnum;
import ch.uzh.ifi.hase.soprafs23.entityDB.CategoryStack;
import ch.uzh.ifi.hase.soprafs23.entityDB.GameUser;
import ch.uzh.ifi.hase.soprafs23.rest.dto.GameGetDTO;
import org.junit.jupiter.api.Test;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

/*
class GameGetDTOTest {
    @Test
    void testGetGameId() {
        GameGetDTO gameGetDTO = new GameGetDTO();
        Long gameId = 12345L;
        gameGetDTO.setGameId(gameId);
        assertEquals(gameId, gameGetDTO.getGameId());
    }

    @Test
    void testGetLobbyCreator() {
        GameGetDTO gameGetDTO = new GameGetDTO();
        GameUser lobbyCreator = new GameUser();
        gameGetDTO.setLobbyCreator(lobbyCreator);
        assertEquals(lobbyCreator, gameGetDTO.getLobbyCreator());
    }

    @Test
    void testGetParticipants() {
        GameGetDTO gameGetDTO = new GameGetDTO();
        Set<GameUser> participants = new HashSet<>();
        GameUser participant1 = new GameUser();
        GameUser participant2 = new GameUser();
        participants.add(participant1);
        participants.add(participant2);
        gameGetDTO.setParticipants(participants);
        assertEquals(participants, gameGetDTO.getParticipants());
    }

    @Test
    void testGetCreationDate() {
        GameGetDTO gameGetDTO = new GameGetDTO();
        Date creationDate = new Date();
        gameGetDTO.setCreationDate(creationDate);
        assertEquals(creationDate, gameGetDTO.getCreationDate());
    }

    @Test
    void testGetCurrentState() {
        GameGetDTO gameGetDTO = new GameGetDTO();
        GameState currentState = GameState.GUESSING;
        gameGetDTO.setCurrentState(currentState);
        assertEquals(currentState, gameGetDTO.getCurrentState());
    }

    @Test
    void testGetGameEndingCriteria() {
        GameGetDTO gameGetDTO = new GameGetDTO();
        long gameEndingCriteria = 10;
        gameGetDTO.setGameEndingCriteria(gameEndingCriteria);
        assertEquals(gameEndingCriteria, gameGetDTO.getGameEndingCriteria());
    }

    @Test
    void testGetRoundDuration() {
        GameGetDTO gameGetDTO = new GameGetDTO();
        long roundDuration = 60;
        gameGetDTO.setRoundDuration(roundDuration);
        assertEquals(roundDuration, gameGetDTO.getRoundDuration());
    }

    @Test
    void testGetRegion() {
        GameGetDTO gameGetDTO = new GameGetDTO();
        RegionEnum region = RegionEnum.ASIA;
        gameGetDTO.setRegion(region);
        assertEquals(region, gameGetDTO.getRegion());
    }

    @Test
    void testGetCurrentRound() {
        GameGetDTO gameGetDTO = new GameGetDTO();
        long currentRound = 1;
        gameGetDTO.setCurrentRound(currentRound);
        assertEquals(currentRound, gameGetDTO.getCurrentRound());
    }

    @Test
    void testGetCategoryStack() {
        GameGetDTO gameGetDTO = new GameGetDTO();
        CategoryStack categoryStack = new CategoryStack();
        gameGetDTO.setCategoryStack(categoryStack);
        assertEquals(categoryStack, gameGetDTO.getCategoryStack());
    }

    @Test
    public void setCurrentRound_validRound_roundSetSuccessfully() {
        GameGetDTO gameDTO = new GameGetDTO();
        long currentRound = 3L;
        gameDTO.setCurrentRound(currentRound);
        assertEquals(currentRound, gameDTO.getCurrentRound());
    }

    @Test
    public void setCategoryStack_validCategoryStack_categoryStackSetSuccessfully() {
        GameGetDTO gameDTO = new GameGetDTO();
        CategoryStack categoryStack = new CategoryStack();
        categoryStack.setCategoryStackId(1L);
        categoryStack.setCurrentCategory(CategoryEnum.CAPITAL);
        gameDTO.setCategoryStack(categoryStack);
        assertEquals(categoryStack, gameDTO.getCategoryStack());
    }

    @Test
    public void setRemainingTime_validRemainingTime_remainingTimeSetSuccessfully() {
        GameGetDTO gameDTO = new GameGetDTO();
        Long remainingTime = 60L;
        gameDTO.setRemainingTime(remainingTime);
        assertEquals(remainingTime, gameDTO.getRemainingTime());
    }
}*/