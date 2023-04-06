package ch.uzh.ifi.hase.soprafs23.StatePattern;

import ch.uzh.ifi.hase.soprafs23.constant.GameState;
import ch.uzh.ifi.hase.soprafs23.constant.WebsocketType;
import ch.uzh.ifi.hase.soprafs23.entityDB.Game;
import ch.uzh.ifi.hase.soprafs23.service.GameService;

public class ScoreboardStateClass implements GameStateClass{
    @Override
    public Game updateGameEverySecond(Game game, GameService gameService) {
        System.out.println("In Scoreboard State Class, updating every Second");
        if (game.getRemainingTime() == 0) {
            if (game.getRemainingRounds() == 0) {
                game.setCurrentState(GameState.ENDED);
                gameService.updateGameState(game.getGameId(), WebsocketType.GAMESTATEUPDATE, game.getCurrentState());
            }
            else {
                //Let SETUP State handle the rest
                game.setRemainingRounds(game.getRemainingRounds() - 1);
                game.setCurrentState(GameState.SETUP);
            }
            return game;
        }
        game.setRemainingTime(game.getRemainingTime() - 1);
        gameService.updateGameState(game.getGameId(), WebsocketType.TIMEUPDATE, game.getRemainingTime());
        return game;
    }
}
