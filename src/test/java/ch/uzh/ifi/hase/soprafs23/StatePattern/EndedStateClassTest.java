package ch.uzh.ifi.hase.soprafs23.StatePattern;

import ch.uzh.ifi.hase.soprafs23.constant.WebsocketType;
import ch.uzh.ifi.hase.soprafs23.entityDB.Game;
import ch.uzh.ifi.hase.soprafs23.service.GameService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class EndedStateClassTest {

    @Test
    public void testUpdateGameEverySecond_remainingTimeIs0() {
        Game mockGame = Mockito.mock(Game.class);
        GameService mockGameService = Mockito.mock(GameService.class);

        when(mockGame.getRemainingTime()).thenReturn(0L);
        when(mockGame.getGameId()).thenReturn(1L);

        EndedStateClass endedStateClass = new EndedStateClass();
        endedStateClass.updateGameEverySecond(mockGame, mockGameService);

        verify(mockGameService).createRestartedGame(mockGame);
        verify(mockGameService).stopGame(1L);
        verify(mockGameService).saveGameToDB(mockGame);
    }

    @Test
    public void testUpdateGameEverySecond_remainingTimeNotZero() {
        Game mockGame = Mockito.mock(Game.class);
        GameService mockGameService = Mockito.mock(GameService.class);
        Long remainingTime = 10L;

        Mockito.when(mockGame.getGameId()).thenReturn(1L);
        Mockito.when(mockGame.getRemainingTime()).thenReturn(remainingTime);

        EndedStateClass endedStateClass = new EndedStateClass();
        endedStateClass.updateGameEverySecond(mockGame, mockGameService);

        verify(mockGame).setRemainingTime(remainingTime - 1);
        verify(mockGameService).updateGameState(1L, WebsocketType.TIMEUPDATE, remainingTime);
    }
}
