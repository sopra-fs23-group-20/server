package ch.uzh.ifi.hase.soprafs23.StatePattern;

import ch.uzh.ifi.hase.soprafs23.constant.GameState;
import ch.uzh.ifi.hase.soprafs23.constant.WebsocketType;
import ch.uzh.ifi.hase.soprafs23.entityDB.Game;
import ch.uzh.ifi.hase.soprafs23.service.GameService;

import java.util.*;

public class SetupStateClass implements GameStateClass {
    @Override
    public Game updateGameEverySecond(Game game, GameService gameService) {
        game.setCurrentState(GameState.GUESSING);
        game.setRemainingRoundPoints(100L);
        Set<Long> allCountriesIds = game.getCountriesToPlayIds();
        game.setGameId(getRandomCountryId(game.getCountriesToPlayIds()));
        game.setRemainingTime(game.getRoundDuration()-1);
        game.setRemainingRounds(game.getNumberOfRounds()-1);
        gameService.updateGameState(game.getGameId(), WebsocketType.GAMESTATEUPDATE, game.getCurrentState());
        gameService.updateGameState(game.getGameId(), WebsocketType.TIMEUPDATE, game.getRemainingTime());
        return game;
    }

    private Long getRandomCountryId(Set<Long> allCountriesIds) {
        List<Long> myList = new ArrayList<>(allCountriesIds);
        Random random = new Random();
        int randomIndex = random.nextInt(myList.size());
        Long randomElement = myList.get(randomIndex);
        return randomElement;
    }
}
