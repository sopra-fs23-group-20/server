package ch.uzh.ifi.hase.soprafs23.StatePattern;

import ch.uzh.ifi.hase.soprafs23.constant.GameState;
import ch.uzh.ifi.hase.soprafs23.constant.RegionEnum;
import ch.uzh.ifi.hase.soprafs23.constant.WebsocketType;
import ch.uzh.ifi.hase.soprafs23.entityDB.CategoryStack;
import ch.uzh.ifi.hase.soprafs23.entityDB.Game;
import ch.uzh.ifi.hase.soprafs23.service.GameService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.mockito.Mockito.*;

public class SetupStateClassTest {

    @Test
    public void testUpdateGameEverySecond() {
        Game mockGame = Mockito.mock(Game.class);
        GameService mockGameService = Mockito.mock(GameService.class);
        CategoryStack mockCategoryStack = Mockito.mock(CategoryStack.class);

        Set<Long> countriesToPlayIds = new HashSet<>();
        countriesToPlayIds.add(1L);

        when(mockGame.getNumberOfRounds()).thenReturn(5L);
        when(mockGame.getGameId()).thenReturn(1L);
        when(mockGame.getRoundDuration()).thenReturn(30L);
        when(mockGame.getCategoryStack()).thenReturn(mockCategoryStack);
        when(mockGame.getCountriesToPlayIds()).thenReturn(countriesToPlayIds);

        SetupStateClass setupStateClass = new SetupStateClass();
        setupStateClass.updateGameEverySecond(mockGame, mockGameService);

        verify(mockGame).setCurrentState(GameState.GUESSING);
        verify(mockGame).setRemainingRounds(4L);
        verify(mockGame).setRemainingRoundPoints(100L);
        verify(mockGameService).updateGameState(eq(1L), eq(WebsocketType.GAMEUPDATE), any());
    }

    @Test
    public void testSelectNewRandomCountry() {
        Game mockGame = Mockito.mock(Game.class);
        GameService mockGameService = Mockito.mock(GameService.class);
        CategoryStack mockCategoryStack = Mockito.mock(CategoryStack.class);

        List<RegionEnum> regions = Collections.singletonList(RegionEnum.EUROPE);
        Set<Long> countryIds = new HashSet<>();
        countryIds.add(1L);

        when(mockGame.getGameId()).thenReturn(1L);
        when(mockGame.getSelectedRegions()).thenReturn(regions);
        when(mockGame.getCountriesToPlayIds()).thenReturn(countryIds);
        when(mockGame.getRoundDuration()).thenReturn(30L);
        when(mockGame.getNumberOfRounds()).thenReturn(3L);
        when(mockGame.getCategoryStack()).thenReturn(mockCategoryStack);

        when(mockGameService.getCountryIdsByRegionsAndDifficulty(any(), any())).thenReturn(countryIds);

        SetupStateClass setupStateClass = new SetupStateClass();
        setupStateClass.updateGameEverySecond(mockGame, mockGameService);

        verify(mockGame).setCountriesToPlayIds(anySet());
        verify(mockGame).setCurrentCountryId(anyLong());
    }

    @Test
    public void testSelectNewRandomCountry_noCountriesLeft() {
        Game mockGame = Mockito.mock(Game.class);
        GameService mockGameService = Mockito.mock(GameService.class);
        CategoryStack mockCategoryStack = Mockito.mock(CategoryStack.class);

        List<RegionEnum> regions = Collections.singletonList(RegionEnum.EUROPE);
        Set<Long> countryIds = new HashSet<>();
        countryIds.add(1L);

        when(mockGame.getGameId()).thenReturn(1L);
        when(mockGame.getSelectedRegions()).thenReturn(regions);
        when(mockGame.getCountriesToPlayIds()).thenReturn(countryIds);
        when(mockGame.getRoundDuration()).thenReturn(30L);
        when(mockGame.getNumberOfRounds()).thenReturn(3L);
        when(mockGame.getCategoryStack()).thenReturn(mockCategoryStack);

        when(mockGameService.getCountryIdsByRegionsAndDifficulty(any(), any())).thenReturn(countryIds);

        SetupStateClass setupStateClass = new SetupStateClass();
        setupStateClass.updateGameEverySecond(mockGame, mockGameService);

        verify(mockGame).setCountriesToPlayIds(anySet());
        verify(mockGame).setCurrentCountryId(anyLong());
    }

    @Test
    public void testSelectNewRandomCountry_noRegionsSelected() {
        Game mockGame = Mockito.mock(Game.class);
        GameService mockGameService = Mockito.mock(GameService.class);
        CategoryStack mockCategoryStack = Mockito.mock(CategoryStack.class);

        List<RegionEnum> regions = new ArrayList<>();
        Set<Long> countryIds = new HashSet<>();
        countryIds.add(1L);

        when(mockGame.getGameId()).thenReturn(1L);
        when(mockGame.getSelectedRegions()).thenReturn(regions);
        when(mockGame.getCountriesToPlayIds()).thenReturn(countryIds);
        when(mockGame.getRoundDuration()).thenReturn(30L);
        when(mockGame.getNumberOfRounds()).thenReturn(3L);
        when(mockGame.getCategoryStack()).thenReturn(mockCategoryStack);

        when(mockGameService.getCountryIdsByRegionsAndDifficulty(any(), any())).thenReturn(countryIds);

        SetupStateClass setupStateClass = new SetupStateClass();
        setupStateClass.updateGameEverySecond(mockGame, mockGameService);

        verify(mockGame).setCountriesToPlayIds(anySet());
        verify(mockGame).setCurrentCountryId(anyLong());
    }

    @Test
    public void testSelectNewRandomCountry_noRegionsSelectedAndNoCountriesLeft() {
        Game mockGame = Mockito.mock(Game.class);
        GameService mockGameService = Mockito.mock(GameService.class);
        CategoryStack mockCategoryStack = Mockito.mock(CategoryStack.class);

        List<RegionEnum> regions = new ArrayList<>();
        Set<Long> countryIds = new HashSet<>();
        countryIds.add(1L);

        when(mockGame.getGameId()).thenReturn(1L);
        when(mockGame.getSelectedRegions()).thenReturn(regions);
        when(mockGame.getCountriesToPlayIds()).thenReturn(countryIds);
        when(mockGame.getRoundDuration()).thenReturn(30L);
        when(mockGame.getNumberOfRounds()).thenReturn(3L);
        when(mockGame.getCategoryStack()).thenReturn(mockCategoryStack);

        when(mockGameService.getCountryIdsByRegionsAndDifficulty(any(), any())).thenReturn(countryIds);

        SetupStateClass setupStateClass = new SetupStateClass();
        setupStateClass.updateGameEverySecond(mockGame, mockGameService);

        verify(mockGame).setCountriesToPlayIds(anySet());
        verify(mockGame).setCurrentCountryId(anyLong());
    }

    @Test
    public void testSelectNewRandomCountry_noRegionsSelectedAndNoCountriesLeftAndNoDifficultySelected() {
        Game mockGame = Mockito.mock(Game.class);
        GameService mockGameService = Mockito.mock(GameService.class);
        CategoryStack mockCategoryStack = Mockito.mock(CategoryStack.class);

        List<RegionEnum> regions = new ArrayList<>();
        Set<Long> countryIds = new HashSet<>();
        countryIds.add(1L);

        when(mockGame.getGameId()).thenReturn(1L);
        when(mockGame.getSelectedRegions()).thenReturn(regions);
        when(mockGame.getCountriesToPlayIds()).thenReturn(countryIds);
        when(mockGame.getRoundDuration()).thenReturn(30L);
        when(mockGame.getNumberOfRounds()).thenReturn(3L);
        when(mockGame.getCategoryStack()).thenReturn(mockCategoryStack);

        when(mockGameService.getCountryIdsByRegionsAndDifficulty(any(), any())).thenReturn(countryIds);

        SetupStateClass setupStateClass = new SetupStateClass();
        setupStateClass.updateGameEverySecond(mockGame, mockGameService);

        verify(mockGame).setCountriesToPlayIds(anySet());
        verify(mockGame).setCurrentCountryId(anyLong());
    }

    @Test
    public void testSelectNewRandomCountry_noRegionsSelectedAndNoCountriesLeftAndNoDifficultySelectedAndNoCategoriesSelected() {
        Game mockGame = Mockito.mock(Game.class);
        GameService mockGameService = Mockito.mock(GameService.class);
        CategoryStack mockCategoryStack = Mockito.mock(CategoryStack.class);

        List<RegionEnum> regions = new ArrayList<>();
        Set<Long> countryIds = new HashSet<>();
        countryIds.add(1L);

        when(mockGame.getGameId()).thenReturn(1L);
        when(mockGame.getSelectedRegions()).thenReturn(regions);
        when(mockGame.getCountriesToPlayIds()).thenReturn(countryIds);
        when(mockGame.getRoundDuration()).thenReturn(30L);
        when(mockGame.getNumberOfRounds()).thenReturn(3L);
        when(mockGame.getCategoryStack()).thenReturn(mockCategoryStack);

        when(mockGameService.getCountryIdsByRegionsAndDifficulty(any(), any())).thenReturn(countryIds);

        SetupStateClass setupStateClass = new SetupStateClass();
        setupStateClass.updateGameEverySecond(mockGame, mockGameService);

        verify(mockGame).setCountriesToPlayIds(anySet());
        verify(mockGame).setCurrentCountryId(anyLong());
    }

    @Test
    public void testSelectNewRandomCountry_noRegionsSelectedAndNoCountriesLeftAndNoDifficultySelectedAndNoCategoriesSelectedAndNoRoundsSelected() {
        Game mockGame = Mockito.mock(Game.class);
        GameService mockGameService = Mockito.mock(GameService.class);
        CategoryStack mockCategoryStack = Mockito.mock(CategoryStack.class);

        List<RegionEnum> regions = new ArrayList<>();
        Set<Long> countryIds = new HashSet<>();
        countryIds.add(1L);

        when(mockGame.getGameId()).thenReturn(1L);
        when(mockGame.getSelectedRegions()).thenReturn(regions);
        when(mockGame.getCountriesToPlayIds()).thenReturn(countryIds);
        when(mockGame.getRoundDuration()).thenReturn(30L);
        when(mockGame.getNumberOfRounds()).thenReturn(3L);
        when(mockGame.getCategoryStack()).thenReturn(mockCategoryStack);

        when(mockGameService.getCountryIdsByRegionsAndDifficulty(any(), any())).thenReturn(countryIds);

        SetupStateClass setupStateClass = new SetupStateClass();
        setupStateClass.updateGameEverySecond(mockGame, mockGameService);

        verify(mockGame).setCountriesToPlayIds(anySet());
        verify(mockGame).setCurrentCountryId(anyLong());
    }

    @Test
    public void testSelectNewRandomCountry_noRegionsSelectedAndNoCountriesLeftAndNoDifficultySelectedAndNoCategoriesSelectedAndNoRoundsSelectedAndNoRoundDurationSelected() {
        Game mockGame = Mockito.mock(Game.class);
        GameService mockGameService = Mockito.mock(GameService.class);
        CategoryStack mockCategoryStack = Mockito.mock(CategoryStack.class);

        List<RegionEnum> regions = new ArrayList<>();
        Set<Long> countryIds = new HashSet<>();
        countryIds.add(1L);

        when(mockGame.getGameId()).thenReturn(1L);
        when(mockGame.getSelectedRegions()).thenReturn(regions);
        when(mockGame.getCountriesToPlayIds()).thenReturn(countryIds);
        when(mockGame.getRoundDuration()).thenReturn(30L);
        when(mockGame.getNumberOfRounds()).thenReturn(3L);
        when(mockGame.getCategoryStack()).thenReturn(mockCategoryStack);

        when(mockGameService.getCountryIdsByRegionsAndDifficulty(any(), any())).thenReturn(countryIds);

        SetupStateClass setupStateClass = new SetupStateClass();
        setupStateClass.updateGameEverySecond(mockGame, mockGameService);

        verify(mockGame).setCountriesToPlayIds(anySet());
        verify(mockGame).setCurrentCountryId(anyLong());
    }

    @Test
    public void testSelectNewRandomCountry_noRegionsSelectedAndNoCountriesLeftAndNoDifficultySelectedAndNoCategoriesSelectedAndNoRoundsSelectedAndNoRoundDurationSelectedAndNoNumberOfPlayersSelected() {
        Game mockGame = Mockito.mock(Game.class);
        GameService mockGameService = Mockito.mock(GameService.class);
        CategoryStack mockCategoryStack = Mockito.mock(CategoryStack.class);

        List<RegionEnum> regions = new ArrayList<>();
        Set<Long> countryIds = new HashSet<>();
        countryIds.add(1L);

        when(mockGame.getGameId()).thenReturn(1L);
        when(mockGame.getSelectedRegions()).thenReturn(regions);
        when(mockGame.getCountriesToPlayIds()).thenReturn(countryIds);
        when(mockGame.getRoundDuration()).thenReturn(30L);
        when(mockGame.getNumberOfRounds()).thenReturn(3L);
        when(mockGame.getCategoryStack()).thenReturn(mockCategoryStack);

        when(mockGameService.getCountryIdsByRegionsAndDifficulty(any(), any())).thenReturn(countryIds);

        SetupStateClass setupStateClass = new SetupStateClass();
        setupStateClass.updateGameEverySecond(mockGame, mockGameService);

        verify(mockGame).setCountriesToPlayIds(anySet());
        verify(mockGame).setCurrentCountryId(anyLong());
    }

    @Test
    public void testSelectNewRandomCountry_noRegionsSelectedAndNoCountriesLeftAndNoDifficultySelectedAndNoCategoriesSelectedAndNoRoundsSelectedAndNoRoundDurationSelectedAndNoNumberOfPlayersSelectedAndNoGameIdSelected() {
        Game mockGame = Mockito.mock(Game.class);
        GameService mockGameService = Mockito.mock(GameService.class);
        CategoryStack mockCategoryStack = Mockito.mock(CategoryStack.class);

        List<RegionEnum> regions = new ArrayList<>();
        Set<Long> countryIds = new HashSet<>();
        countryIds.add(1L);

        when(mockGame.getGameId()).thenReturn(1L);
        when(mockGame.getSelectedRegions()).thenReturn(regions);
        when(mockGame.getCountriesToPlayIds()).thenReturn(countryIds);
        when(mockGame.getRoundDuration()).thenReturn(30L);
        when(mockGame.getNumberOfRounds()).thenReturn(3L);
        when(mockGame.getCategoryStack()).thenReturn(mockCategoryStack);

        when(mockGameService.getCountryIdsByRegionsAndDifficulty(any(), any())).thenReturn(countryIds);

        SetupStateClass setupStateClass = new SetupStateClass();
        setupStateClass.updateGameEverySecond(mockGame, mockGameService);

        verify(mockGame).setCountriesToPlayIds(anySet());
        verify(mockGame).setCurrentCountryId(anyLong());
    }

    @Test
    public void testSelectNewRandomCountry_noRegionsSelectedAndNoCountriesLeftAndNoDifficultySelectedAndNoCategoriesSelectedAndNoRoundsSelectedAndNoRoundDurationSelectedAndNoNumberOfPlayersSelectedAndNoGameIdSelectedAndNoCategoryStackSelected() {
        Game mockGame = Mockito.mock(Game.class);
        GameService mockGameService = Mockito.mock(GameService.class);
        CategoryStack mockCategoryStack = Mockito.mock(CategoryStack.class);

        List<RegionEnum> regions = new ArrayList<>();
        Set<Long> countryIds = new HashSet<>();
        countryIds.add(1L);

        when(mockGame.getGameId()).thenReturn(1L);
        when(mockGame.getSelectedRegions()).thenReturn(regions);
        when(mockGame.getCountriesToPlayIds()).thenReturn(countryIds);
        when(mockGame.getRoundDuration()).thenReturn(30L);
        when(mockGame.getNumberOfRounds()).thenReturn(3L);
        when(mockGame.getCategoryStack()).thenReturn(mockCategoryStack);

        when(mockGameService.getCountryIdsByRegionsAndDifficulty(any(), any())).thenReturn(countryIds);

        SetupStateClass setupStateClass = new SetupStateClass();
        setupStateClass.updateGameEverySecond(mockGame, mockGameService);

        verify(mockGame).setCountriesToPlayIds(anySet());
        verify(mockGame).setCurrentCountryId(anyLong());
    }

}
