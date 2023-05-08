package ch.uzh.ifi.hase.soprafs23.StatePattern;

import ch.uzh.ifi.hase.soprafs23.constant.CategoryEnum;
import ch.uzh.ifi.hase.soprafs23.constant.GameState;
import ch.uzh.ifi.hase.soprafs23.constant.WebsocketType;
import ch.uzh.ifi.hase.soprafs23.entityDB.CategoryStack;
import ch.uzh.ifi.hase.soprafs23.entityDB.Game;
import ch.uzh.ifi.hase.soprafs23.entityDB.Category;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.GameService;

public class GuessingStateClass implements GameStateClass{
    @Override
    public Game updateGameEverySecond(Game game, GameService gameService) {
        System.out.println("In GuessingState Class, updating every Second");
        if (game.getRemainingTime() == 0) {
            if(game.getRemainingRounds() == 0){
                game.setCurrentState(GameState.ENDED);
                game.setRemainingTime(20L);
                gameService.updateGameState(game.getGameId(), WebsocketType.GAMESTATEUPDATE, game.getCurrentState());
                gameService.updateGameState(game.getGameId(), WebsocketType.TIMEUPDATE, game.getRemainingTime());
                return game;
            }else {
                game.setCurrentState(GameState.SCOREBOARD);
                game.setRemainingTime(game.getTimeBetweenRounds() - 1);
                gameService.updateGameState(game.getGameId(), WebsocketType.GAMEUPDATE, DTOMapper.INSTANCE.convertEntityToGameGetDTO(game));
                return game;
            }
        }

        //Make a Category Update
        Long timeBetweenCategoryUpdates = game.getRoundDuration() / (game.getCategoryStack().getSelectedCategories().size());
        System.out.println("Time between Category Updates: " + timeBetweenCategoryUpdates);
        System.out.println("Time between times size " + timeBetweenCategoryUpdates * game.getCategoryStack().getSelectedCategories().size());
        if (game.getRemainingTime() < timeBetweenCategoryUpdates * game.getCategoryStack().getRemainingCategories().size()) {
            if(!game.getCategoryStack().isEmpty()){
                CategoryStack categoryStack = game.getCategoryStack();
                CategoryEnum categoryEnum = categoryStack.pop();
                Category currentCategory = gameService.transformToCategory(categoryEnum, game.getCurrentCountryId());
                categoryStack.setCurrentCategory(currentCategory);
                gameService.updateGameState(game.getGameId(), WebsocketType.CATEGORYUPDATE, categoryStack);
            }
        }
        //Reduce Time and Points
        game.setRemainingTime(game.getRemainingTime() - 1);
        gameService.updateGameState(game.getGameId(), WebsocketType.TIMEUPDATE, game.getRemainingTime());
        reduceCurrentPoints(game);
        gameService.updateGameState(game.getGameId(), WebsocketType.POINTSUPDATE, game.getRemainingRoundPoints());
        return game;
    }
    private void reduceCurrentPoints(Game game) {

        long roundTime = game.getRoundDuration();
        double pointsDeducted = 100.0 / roundTime;
        Long pDeducted = (long) Math.floor(pointsDeducted);
        Long newCurrentPoints = game.getRemainingRoundPoints() - pDeducted;
        game.setRemainingRoundPoints(newCurrentPoints);

    }
}
