package ch.uzh.ifi.hase.soprafs23.rest.dto;

import ch.uzh.ifi.hase.soprafs23.constant.Difficulty;
import ch.uzh.ifi.hase.soprafs23.constant.GameState;
import ch.uzh.ifi.hase.soprafs23.entityDB.CategoryStack;
import ch.uzh.ifi.hase.soprafs23.entityDB.GameUser;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.Date;
import java.util.List;
import ch.uzh.ifi.hase.soprafs23.constant.RegionEnum;
import java.util.Set;

public class GameGetDTO {
    private Long gameId;
    @JsonManagedReference
    private GameUser lobbyCreator;
    @JsonManagedReference
    private Set<GameUser> participants;
    private Date creationDate;
    private GameState currentState;
    private Long roundDuration;
    private Long remainingTime;
    private Long numberOfRounds;
    private Long remainingRounds;
    private Long remainingRoundPoints;
    private List<RegionEnum> selectedRegions;
    private CategoryStack categoryStack;
    private Boolean openLobby;
    private Difficulty difficulty;
    private Long timeBetweenRounds;

    private Long nextGameId;

    public Long getNextGameId() {
        return nextGameId;
    }

    public void setNextGameId(Long nextGameId) {
        this.nextGameId = nextGameId;
    }

    public Long getTimeBetweenRounds() {
        return timeBetweenRounds;
    }

    public void setTimeBetweenRounds(Long timeBetweenRounds) {
        this.timeBetweenRounds = timeBetweenRounds;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
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

    public Set<GameUser> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<GameUser> participants) {
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

    public Long getRoundDuration() {
        return roundDuration;
    }

    public void setRoundDuration(Long roundDuration) {
        this.roundDuration = roundDuration;
    }

    public Long getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(Long remainingTime) {
        this.remainingTime = remainingTime;
    }

    public Long getNumberOfRounds() {
        return numberOfRounds;
    }

    public void setNumberOfRounds(Long numberOfRounds) {
        this.numberOfRounds = numberOfRounds;
    }

    public Long getRemainingRounds() {
        return remainingRounds;
    }

    public void setRemainingRounds(Long remainingRounds) {
        this.remainingRounds = remainingRounds;
    }

    public Long getRemainingRoundPoints() {
        return remainingRoundPoints;
    }

    public void setRemainingRoundPoints(Long remainingRoundPoints) {
        this.remainingRoundPoints = remainingRoundPoints;
    }


    public List<RegionEnum> getSelectedRegions() {
        return selectedRegions;
    }

    public void setSelectedRegions(List<RegionEnum> selectedRegions) {
        this.selectedRegions = selectedRegions;
    }

    public CategoryStack getCategoryStack() {
        return categoryStack;
    }

    public void setCategoryStack(CategoryStack categoryStack) {
        this.categoryStack = categoryStack;
    }

    public Boolean getOpenLobby() {
        return openLobby;
    }

    public void setOpenLobby(Boolean openLobby) {
        this.openLobby = openLobby;
    }
}