package ch.uzh.ifi.hase.soprafs23.StatePattern;

import ch.uzh.ifi.hase.soprafs23.constant.GameState;
import ch.uzh.ifi.hase.soprafs23.constant.WebsocketType;
import ch.uzh.ifi.hase.soprafs23.entityDB.Game;
import ch.uzh.ifi.hase.soprafs23.service.GameService;

public class ScoreboardStateClass implements GameStateClass{
    @Override
    public Game updateGameEverySecond(Game game, GameService gameService) {
        if(game.getRemainingRounds() == 0){
            game.setCurrentState(GameState.ENDED);
            return game;
        }
        if(game.getRemainingTime()>0) {
            game.setRemainingTime(game.getRemainingTime() - 1);
            gameService.updateGameState(game.getGameId(), WebsocketType.TIMEUPDATE, game.getRemainingTime());
            return game;
        }
        game.setRemainingTime(game.get);
        return game;
    }
}
