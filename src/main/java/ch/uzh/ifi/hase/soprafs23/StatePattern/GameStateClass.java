package ch.uzh.ifi.hase.soprafs23.StatePattern;

import ch.uzh.ifi.hase.soprafs23.entityDB.Game;
import ch.uzh.ifi.hase.soprafs23.service.GameService;

public interface GameStateClass {
    public Game updateGameEverySecond(Game game, GameService gameService);
}
