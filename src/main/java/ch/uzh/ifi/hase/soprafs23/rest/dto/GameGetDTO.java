package ch.uzh.ifi.hase.soprafs23.rest.dto;

import ch.uzh.ifi.hase.soprafs23.constant.CategoryEnum;
import ch.uzh.ifi.hase.soprafs23.constant.GameState;
import ch.uzh.ifi.hase.soprafs23.constant.RegionEnum;
import ch.uzh.ifi.hase.soprafs23.entity.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Stack;

public class GameGetDTO {
    private Long gameId;
    private GameUser lobbyCreator;

    private List<GameUser> participants;

    private Date creationDate;
    private GameState currentState;
    private long gameEndingCriteria;
    private long roundDuration;
    private RegionEnum region;
    private List<Country> countriesToPlay;
    private long currentRound;

    private List<CategoryEnum> categoriesSelected;
    private Category currentCategory;
    private Long remainingTime;
    private GameCountry currentCountry;

    private CategoryStack remainingCategories;

    public CategoryStack getRemainingCategories() {
        return remainingCategories;
    }

    public void setRemainingCategories(CategoryStack remainingCategories) {
        this.remainingCategories = remainingCategories;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public GameUser getLobbyCreator() {
        return lobbyCreator;
    }

    public void setLobbyCreator(GameUser lobbyCreator) {
        this.lobbyCreator = lobbyCreator;
    }

    public List<GameUser> getParticipants() {
        return participants;
    }

    public void setParticipants(List<GameUser> participants) {
        this.participants = participants;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public GameState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(GameState currentState) {
        this.currentState = currentState;
    }

    public long getGameEndingCriteria() {
        return gameEndingCriteria;
    }

    public void setGameEndingCriteria(long gameEndingCriteria) {
        this.gameEndingCriteria = gameEndingCriteria;
    }

    public long getRoundDuration() {
        return roundDuration;
    }

    public void setRoundDuration(long roundDuration) {
        this.roundDuration = roundDuration;
    }

    public RegionEnum getRegion() {
        return region;
    }

    public void setRegion(RegionEnum region) {
        this.region = region;
    }

    public List<Country> getCountriesToPlay() {
        return countriesToPlay;
    }

    public void setCountriesToPlay(List<Country> countriesToPlay) {
        this.countriesToPlay = countriesToPlay;
    }

    public long getCurrentRound() {
        return currentRound;
    }

    public void setCurrentRound(long currentRound) {
        this.currentRound = currentRound;
    }



    public List<CategoryEnum> getCategoriesSelected() {
        return categoriesSelected;
    }

    public void setCategoriesSelected(List<CategoryEnum> categoriesSelected) {
        this.categoriesSelected = categoriesSelected;
    }

    public Category getCurrentCategory() {
        return currentCategory;
    }

    public void setCurrentCategory(Category currentCategory) {
        this.currentCategory = currentCategory;
    }

    public Long getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(Long remainingTime) {
        this.remainingTime = remainingTime;
    }

    public GameCountry getCurrentCountry() {
        return currentCountry;
    }

    public void setCurrentCountry(GameCountry currentCountry) {
        this.currentCountry = currentCountry;
    }
}
