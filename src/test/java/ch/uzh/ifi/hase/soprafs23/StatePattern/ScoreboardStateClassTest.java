package ch.uzh.ifi.hase.soprafs23.StatePattern;

import ch.uzh.ifi.hase.soprafs23.constant.GameState;
import ch.uzh.ifi.hase.soprafs23.constant.WebsocketType;
import ch.uzh.ifi.hase.soprafs23.entityDB.CategoryStack;
import ch.uzh.ifi.hase.soprafs23.entityDB.Game;
import ch.uzh.ifi.hase.soprafs23.service.GameService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;

public class ScoreboardStateClassTest {

    @Test
    public void testUpdateGameEverySecond_remainingRoundsIs0() {
        Game mockGame = Mockito.mock(Game.class);
        GameService mockGameService = Mockito.mock(GameService.class);

        when(mockGame.getRemainingTime()).thenReturn(0L);
        when(mockGame.getRemainingRounds()).thenReturn(0L);
        when(mockGame.getGameId()).thenReturn(1L);
        when(mockGame.getCurrentState()).thenReturn(GameState.ENDED);

        ScoreboardStateClass scoreboardStateClass = new ScoreboardStateClass();
        scoreboardStateClass.updateGameEverySecond(mockGame, mockGameService);

        verify(mockGame).setCurrentState(GameState.ENDED);
        verify(mockGameService).updateGameState(eq(1L), eq(WebsocketType.GAMESTATEUPDATE), eq(GameState.ENDED));
    }

    @Test
    public void testUpdateGameEverySecond_remainingRoundsNot0() {
        Game mockGame = Mockito.mock(Game.class);
        GameService mockGameService = Mockito.mock(GameService.class);
        CategoryStack mockCategoryStack = Mockito.mock(CategoryStack.class);

        Set<Long> countriesToPlayIds = new HashSet<>();
        countriesToPlayIds.add(1L);

        when(mockGame.getRemainingTime()).thenReturn(0L);
        when(mockGame.getRemainingRounds()).thenReturn(1L);
        when(mockGame.getGameId()).thenReturn(1L);
        when(mockGame.getCountriesToPlayIds()).thenReturn(countriesToPlayIds);
        when(mockGame.getCategoryStack()).thenReturn(mockCategoryStack);

        ScoreboardStateClass scoreboardStateClass = new ScoreboardStateClass();
        scoreboardStateClass.updateGameEverySecond(mockGame, mockGameService);

        verify(mockGame).setCurrentState(GameState.GUESSING);
        verify(mockGameService).updateGameState(eq(1L), eq(WebsocketType.GAMEUPDATE), any());
    }
}

