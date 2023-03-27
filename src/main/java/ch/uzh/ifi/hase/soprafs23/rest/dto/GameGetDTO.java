package ch.uzh.ifi.hase.soprafs23.rest.dto;

import ch.uzh.ifi.hase.soprafs23.constant.CategoryEnum;
import ch.uzh.ifi.hase.soprafs23.constant.GameState;
import ch.uzh.ifi.hase.soprafs23.constant.RegionEnum;
import ch.uzh.ifi.hase.soprafs23.entityDB.CategoryStack;
import ch.uzh.ifi.hase.soprafs23.entityDB.Country;
import ch.uzh.ifi.hase.soprafs23.entityDB.GameUser;
import ch.uzh.ifi.hase.soprafs23.entityOther.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class GameGetDTO {
    private Long gameId;
    private GameUser lobbyCreator;
    private Set<GameUser> participants;

    private Date creationDate;
    private GameState currentState;
    private long gameEndingCriteria;
    private long roundDuration;
    private RegionEnum region;
    private long currentRound;
    private CategoryStack categoryStack;
    private Long remainingTime;

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

    public long getCurrentRound() {
        return currentRound;
    }

    public void setCurrentRound(long currentRound) {
        this.currentRound = currentRound;
    }

    public CategoryStack getCategoryStack() {
        return categoryStack;
    }

    public void setCategoryStack(CategoryStack categoryStack) {
        this.categoryStack = categoryStack;
    }

    public Long getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(Long remainingTime) {
        this.remainingTime = remainingTime;
    }

}
