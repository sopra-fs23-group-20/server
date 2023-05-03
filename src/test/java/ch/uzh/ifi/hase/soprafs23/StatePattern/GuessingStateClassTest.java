package ch.uzh.ifi.hase.soprafs23.StatePattern;

import ch.uzh.ifi.hase.soprafs23.constant.CategoryEnum;
import ch.uzh.ifi.hase.soprafs23.constant.GameState;
import ch.uzh.ifi.hase.soprafs23.constant.WebsocketType;
import ch.uzh.ifi.hase.soprafs23.entityDB.CategoryStack;
import ch.uzh.ifi.hase.soprafs23.entityDB.Game;
import ch.uzh.ifi.hase.soprafs23.service.GameService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.Mockito.*;

public class GuessingStateClassTest {

    private ArrayList<CategoryEnum> createSelectedCategoriesList() {
        return new ArrayList<>(Arrays.asList(CategoryEnum.POPULATION, CategoryEnum.CAPITAL));
    }

    @Test
    public void testUpdateGameEverySecond() {
        Game mockGame = Mockito.mock(Game.class);
        GameService mockGameService = Mockito.mock(GameService.class);
        CategoryStack mockCategoryStack = Mockito.mock(CategoryStack.class);

        when(mockGame.getRemainingTime()).thenReturn(0L);
        when(mockGame.getRemainingRounds()).thenReturn(0L);
        when(mockGame.getGameId()).thenReturn(1L);
        when(mockGame.getCurrentState()).thenReturn(GameState.ENDED);
        when(mockGame.getCategoryStack()).thenReturn(mockCategoryStack);
        when(mockGame.getRoundDuration()).thenReturn(30L);
        when(mockCategoryStack.getSelectedCategories()).thenReturn(createSelectedCategoriesList());
        when(mockCategoryStack.getRemainingCategories()).thenReturn(createSelectedCategoriesList());
        when(mockCategoryStack.isEmpty()).thenReturn(true);

        GuessingStateClass guessingStateClass = new GuessingStateClass();
        guessingStateClass.updateGameEverySecond(mockGame, mockGameService);

        verify(mockGameService).updateGameState(eq(1L), eq(WebsocketType.GAMESTATEUPDATE), eq(GameState.ENDED));
        //verify(mockGameService, atLeastOnce()).updateGameState(eq(1L), eq(WebsocketType.TIMEUPDATE), anyLong());
        //verify(mockGameService, atLeastOnce()).updateGameState(eq(1L), eq(WebsocketType.POINTSUPDATE), anyLong());
    }


    @Test
    public void testUpdateGameEverySecond_remainingTimeNot1() {
        Game mockGame = Mockito.mock(Game.class);
        GameService mockGameService = Mockito.mock(GameService.class);
        CategoryStack mockCategoryStack = Mockito.mock(CategoryStack.class);

        when(mockGame.getRemainingTime()).thenReturn(2L);
        when(mockGame.getGameId()).thenReturn(1L);
        when(mockGame.getCategoryStack()).thenReturn(mockCategoryStack);
        when(mockGame.getRoundDuration()).thenReturn(30L);
        when(mockCategoryStack.getSelectedCategories()).thenReturn(createSelectedCategoriesList());
        when(mockCategoryStack.getRemainingCategories()).thenReturn(createSelectedCategoriesList());
        when(mockCategoryStack.isEmpty()).thenReturn(true);

        GuessingStateClass guessingStateClass = new GuessingStateClass();
        guessingStateClass.updateGameEverySecond(mockGame, mockGameService);

        verify(mockGameService).updateGameState(eq(1L), eq(WebsocketType.TIMEUPDATE), eq(2L));
        verify(mockGameService).updateGameState(eq(1L), eq(WebsocketType.POINTSUPDATE), any());
    }

    @Test
    public void testUpdateGameEverySecond_remainingRoundsNot0() {
        Game mockGame = Mockito.mock(Game.class);
        GameService mockGameService = Mockito.mock(GameService.class);
        CategoryStack mockCategoryStack = Mockito.mock(CategoryStack.class);

        when(mockGame.getRemainingTime()).thenReturn(0L);
        when(mockGame.getRemainingRounds()).thenReturn(0L);
        when(mockGame.getGameId()).thenReturn(1L);
        when(mockGame.getCategoryStack()).thenReturn(mockCategoryStack);
        when(mockGame.getRoundDuration()).thenReturn(30L);
        when(mockCategoryStack.getSelectedCategories()).thenReturn(createSelectedCategoriesList());
        when(mockCategoryStack.getRemainingCategories()).thenReturn(new ArrayList<>());
        when(mockCategoryStack.isEmpty()).thenReturn(true);
        when(mockGame.getCurrentState()).thenReturn(GameState.SCOREBOARD);

        GuessingStateClass guessingStateClass = new GuessingStateClass();
        guessingStateClass.updateGameEverySecond(mockGame, mockGameService);

        verify(mockGameService).updateGameState(eq(1L), eq(WebsocketType.GAMESTATEUPDATE), eq(GameState.SCOREBOARD));
        //verify(mockGameService, atLeastOnce()).updateGameState(eq(1L), eq(WebsocketType.POINTSUPDATE), any());
    }

    @Test
    public void testUpdateGameEverySecond_categoryStackNotEmpty() {
        Game mockGame = Mockito.mock(Game.class);
        GameService mockGameService = Mockito.mock(GameService.class);
        CategoryStack mockCategoryStack = Mockito.mock(CategoryStack.class);

        when(mockGame.getRemainingTime()).thenReturn(10L);
        when(mockGame.getGameId()).thenReturn(1L);
        when(mockGame.getCategoryStack()).thenReturn(mockCategoryStack);
        when(mockGame.getRoundDuration()).thenReturn(30L);
        when(mockCategoryStack.getSelectedCategories()).thenReturn(createSelectedCategoriesList());
        when(mockCategoryStack.getRemainingCategories()).thenReturn(createSelectedCategoriesList());
        when(mockCategoryStack.isEmpty()).thenReturn(false);

        GuessingStateClass guessingStateClass = new GuessingStateClass();
        guessingStateClass.updateGameEverySecond(mockGame, mockGameService);

        verify(mockGameService, atLeastOnce()).updateGameState(eq(1L), eq(WebsocketType.CATEGORYUPDATE), any());
        verify(mockGameService, atLeastOnce()).updateGameState(eq(1L), eq(WebsocketType.TIMEUPDATE), any());
        verify(mockGameService, atLeastOnce()).updateGameState(eq(1L), eq(WebsocketType.POINTSUPDATE), any());

    }



}
