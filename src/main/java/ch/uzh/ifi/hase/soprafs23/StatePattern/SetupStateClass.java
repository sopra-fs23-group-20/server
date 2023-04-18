package ch.uzh.ifi.hase.soprafs23.StatePattern;

import ch.uzh.ifi.hase.soprafs23.constant.CategoryEnum;
import ch.uzh.ifi.hase.soprafs23.constant.GameState;
import ch.uzh.ifi.hase.soprafs23.constant.WebsocketType;
import ch.uzh.ifi.hase.soprafs23.entityDB.Category;
import ch.uzh.ifi.hase.soprafs23.entityDB.CategoryStack;
import ch.uzh.ifi.hase.soprafs23.entityDB.Game;
import ch.uzh.ifi.hase.soprafs23.entityDB.GameUser;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.GameService;

import java.util.*;

public class SetupStateClass implements GameStateClass {
    @Override
    public Game updateGameEverySecond(Game game, GameService gameService) {
        System.out.println("In Setup State Class, updating every Second");
        game.setCurrentState(GameState.GUESSING);
        game.setRemainingRounds(game.getNumberOfRounds()-1);
        game.setRemainingRoundPoints(100L);
        Set<Long> allCountriesIds = game.getCountriesToPlayIds();
        selectNewRandomCountry(game);
        game.getCategoryStack().refillStack();
        CategoryStack categoryStack = game.getCategoryStack();
        CategoryEnum categoryEnum = categoryStack.pop();
        Category currentCategory = gameService.transformToCategory(categoryEnum, game.getCurrentCountryId());
        categoryStack.setCurrentCategory(currentCategory);
        resetAlreadyGuess(game);
        game.setRemainingTime(game.getRoundDuration());
        gameService.updateGameState(game.getGameId(), WebsocketType.GAMEUPDATE, DTOMapper.INSTANCE.convertEntityToGameGetDTO(game));
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
    private void resetAlreadyGuess(Game game){
        for (GameUser gameUser : game.getParticipants()) {
            gameUser.setHasAlreadyGuessed(false);
        }
    }
}
