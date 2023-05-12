package ch.uzh.ifi.hase.soprafs23.StatePattern;

import ch.uzh.ifi.hase.soprafs23.constant.CategoryEnum;
import ch.uzh.ifi.hase.soprafs23.constant.GameMode;
import ch.uzh.ifi.hase.soprafs23.constant.GameState;
import ch.uzh.ifi.hase.soprafs23.constant.WebsocketType;
import ch.uzh.ifi.hase.soprafs23.entityDB.Category;
import ch.uzh.ifi.hase.soprafs23.entityDB.CategoryStack;
import ch.uzh.ifi.hase.soprafs23.entityDB.Game;
import ch.uzh.ifi.hase.soprafs23.entityDB.GameUser;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.GameService;

import java.util.*;

public class ScoreboardStateClass implements GameStateClass{
    @Override
    public Game updateGameEverySecond(Game game, GameService gameService) {
        if (game.getRemainingTime() == 0) {
            if (game.getRemainingRounds() == 0) {
                game.setCurrentState(GameState.ENDED);
                game.setRemainingTime(30L);
                gameService.updateGameState(game.getGameId(), WebsocketType.GAMESTATEUPDATE, game.getCurrentState());
                gameService.updateGameState(game.getGameId(), WebsocketType.TIMEUPDATE, game.getRemainingTime());
                return game;
            }
            else {
                game.setRemainingRounds(game.getRemainingRounds() - 1);
                game.setCurrentState(GameState.GUESSING);
                game.setRemainingRoundPoints(100L);
                selectNewRandomCountry(game, gameService);
                game.setRemainingTime(game.getRoundDuration() - 1);
                resetAlreadyGuess(game);
                game.getCategoryStack().refillStack();
                CategoryStack categoryStack = game.getCategoryStack();
                CategoryEnum categoryEnum = categoryStack.pop();
                Category currentCategory = gameService.transformToCategory(categoryEnum, game.getCurrentCountryId());
                categoryStack.setCurrentCategory(currentCategory);
                gameService.updateGameState(game.getGameId(), WebsocketType.GAMEUPDATE, DTOMapper.INSTANCE.convertEntityToGameGetDTO(game));
                return game;
            }

        }
        game.setRemainingTime(game.getRemainingTime() - 1);
        gameService.updateGameState(game.getGameId(), WebsocketType.TIMEUPDATE, game.getRemainingTime());
        return game;
    }

    private void selectNewRandomCountry(Game game,GameService gameService) {
        List<Long> myList = new ArrayList<>(game.getCountriesToPlayIds());
        if(myList.isEmpty()){
            game.setCountriesToPlayIds(gameService.getCountryIdsByRegionsAndDifficulty(game.getSelectedRegions(),game.getDifficulty()));
            myList = new ArrayList<>(game.getCountriesToPlayIds());
        }
        Random random = new Random();
        int randomIndex = random.nextInt(myList.size());
        game.setCurrentCountryId(myList.get(randomIndex));
        myList.remove(randomIndex);
        game.setCountriesToPlayIds(new HashSet<>(myList));
        if(game.getGameMode() == GameMode.BLITZ){
            gameService.setNewClosestCountries(game);
        }
    }

    private void resetAlreadyGuess(Game game){
        for (GameUser gameUser : game.getParticipants()) {
            gameUser.setHasAlreadyGuessed(false);
        }
    }
}
