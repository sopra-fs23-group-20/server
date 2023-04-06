package ch.uzh.ifi.hase.soprafs23.StatePattern;

import ch.uzh.ifi.hase.soprafs23.constant.GameState;
import ch.uzh.ifi.hase.soprafs23.constant.WebsocketType;
import ch.uzh.ifi.hase.soprafs23.entityDB.Game;
import ch.uzh.ifi.hase.soprafs23.service.GameService;

import java.util.*;

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
                game.setCurrentState(GameState.GUESSING);
                game.setRemainingRoundPoints(100L);
                Set<Long> allCountriesIds = game.getCountriesToPlayIds();
                selectNewRandomCountry(game);
                game.setRemainingTime(game.getRoundDuration());
                gameService.updateGameState(game.getGameId(), WebsocketType.GAMESTATEUPDATE, game.getCurrentState());
                gameService.updateGameState(game.getGameId(), WebsocketType.TIMEUPDATE, game.getRemainingTime());
            }
            return game;
        }
        game.setRemainingTime(game.getRemainingTime() - 1);
        gameService.updateGameState(game.getGameId(), WebsocketType.TIMEUPDATE, game.getRemainingTime());
        return game;
    }

    private void selectNewRandomCountry(Game game) {
        List<Long> myList = new ArrayList<>(game.getCountriesToPlayIds());
        Random random = new Random();
        int randomIndex = random.nextInt(myList.size());
        game.setCurrentCountryId(myList.get(randomIndex));
        myList.remove(randomIndex);
        game.setCountriesToPlayIds(new HashSet<>(myList));
    }
}
