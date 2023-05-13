package ch.uzh.ifi.hase.soprafs23.StatePattern;

import ch.uzh.ifi.hase.soprafs23.constant.WebsocketType;
import ch.uzh.ifi.hase.soprafs23.entityDB.Game;
import ch.uzh.ifi.hase.soprafs23.service.GameService;

public class EndedStateClass implements GameStateClass{
    @Override
    public Game updateGameEverySecond(Game game, GameService gameService) {
        if(game.getRemainingTime()==0){
            gameService.createRestartedGame(game);
            gameService.stopGame(game.getGameId());
            gameService.saveGameToDB(game);
            return game;
        }
        game.setRemainingTime(game.getRemainingTime()-1);

            gameService.updateGameState(game.getGameId(), WebsocketType.TIMEUPDATE, game.getRemainingTime());
        return game;
    }
}
