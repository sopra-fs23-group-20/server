package ch.uzh.ifi.hase.soprafs23.StatePattern;

import ch.uzh.ifi.hase.soprafs23.entityDB.Game;
import ch.uzh.ifi.hase.soprafs23.service.GameService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.verify;

public class EndedStateClassTest {

    @Test
    public void testUpdateGameEverySecond() {
        Game mockGame = Mockito.mock(Game.class);
        GameService mockGameService = Mockito.mock(GameService.class);

        Mockito.when(mockGame.getGameId()).thenReturn(1L);

        EndedStateClass endedStateClass = new EndedStateClass();
        endedStateClass.updateGameEverySecond(mockGame, mockGameService);

        verify(mockGameService).stopGame(1L);
    }
}
