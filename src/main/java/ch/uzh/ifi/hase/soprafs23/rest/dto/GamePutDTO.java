package ch.uzh.ifi.hase.soprafs23.rest.dto;

import ch.uzh.ifi.hase.soprafs23.constant.CategoryEnum;

import java.util.List;

public class GamePutDTO {

    private String lobbyCreatorUserId;
    private int roundSeconds;
    private boolean randomizedHints;
    private boolean allCountries;
    private int numberOfRounds;
    private boolean openLobby;
    private List<CategoryEnum> hints;
    private Long gameId;

    public int getRoundSeconds() {
        return roundSeconds;
    }

    public void setRoundSeconds(int roundSeconds) {
        this.roundSeconds = roundSeconds;
    }

    public boolean isRandomizedHints() {
        return randomizedHints;
    }

    public void setRandomizedHints(boolean randomizedHints) {
        this.randomizedHints = randomizedHints;
    }

    public boolean isAllCountries() {
        return allCountries;
    }

    public void setAllCountries(boolean allCountries) {
        this.allCountries = allCountries;
    }

    public int getNumberOfRounds() {
        return numberOfRounds;
    }

    public void setNumberOfRounds(int numberOfRounds) {
        this.numberOfRounds = numberOfRounds;
    }

    public boolean isOpenLobby() {
        return openLobby;
    }

    public void setOpenLobby(boolean openLobby) {
        this.openLobby = openLobby;
    }

    public List<CategoryEnum> getHints() {
        return hints;
    }

    public void setHints(List<CategoryEnum> hints) {
        this.hints = hints;
    }

    public String getLobbyCreatorUserId() {
        return lobbyCreatorUserId;
    }

    public void setLobbyCreatorUserId(String lobbyCreatorUserId) {
        this.lobbyCreatorUserId = lobbyCreatorUserId;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }
}
