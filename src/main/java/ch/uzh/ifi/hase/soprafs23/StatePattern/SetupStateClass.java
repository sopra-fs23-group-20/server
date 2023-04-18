package ch.uzh.ifi.hase.soprafs23.StatePattern;

import ch.uzh.ifi.hase.soprafs23.constant.GameState;
import ch.uzh.ifi.hase.soprafs23.constant.WebsocketType;
import ch.uzh.ifi.hase.soprafs23.entityDB.Game;
import ch.uzh.ifi.hase.soprafs23.entityDB.GameUser;
import ch.uzh.ifi.hase.soprafs23.service.GameService;

import java.util.*;

public class SetupStateClass implements GameStateClass {
    @Override
    public Game updateGameEverySecond(Game game, GameService gameService) {
        System.out.println("In Setup State Class, updating every Second");
        game.setCurrentState(GameState.GUESSING);
        game.setRemainingRoundPoints(100L);
        selectNewRandomCountry(game);
        game.setRemainingTime(game.getRoundDuration());
        gameService.updateGameState(game.getGameId(), WebsocketType.GAMESTATEUPDATE, game.getCurrentState());
        gameService.updateGameState(game.getGameId(), WebsocketType.TIMEUPDATE, game.getRemainingTime());
        return game;
    }

    private void selectNewRandomCountry(Game game) {
        List<Long> myList = new ArrayList<>(game.getCountriesToPlayIds());
        System.out.println("Countries to play: " + myList);
        Random random = new Random();
        int randomIndex = random.nextInt(myList.size());
        game.setCurrentCountryId(myList.get(randomIndex));
        myList.remove(randomIndex);
        game.setCountriesToPlayIds(new HashSet<>(myList));
    }

    private void setAllGameUsers(Game game){
        Set<GameUser> gameUsers = game.getParticipants();
        for(GameUser gameUser : gameUsers){
            gameUser.setUserPlayingState(GameState.SETUP);
            gameUser.setGamePoints(100L);
        }
        game.setCurrentState(GameState.SETUP);
        game.setRemainingRoundPoints(100L);
        game.setRemainingTime(game.getRoundDuration());
    }

}
