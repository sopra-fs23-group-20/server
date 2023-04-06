package ch.uzh.ifi.hase.soprafs23.rest.dto;

import ch.uzh.ifi.hase.soprafs23.constant.CategoryEnum;

import java.util.List;

public class GamePostDTO {

    private String lobbyCreatorUserId;

    private Long roundSeconds;
    private boolean randomizedHints;
    private boolean allCountries;
    private Long numberOfRounds;
    private boolean openLobby;
    private List<CategoryEnum> hints;

    public Long getRoundSeconds() {
        return roundSeconds;
    }

    public void setRoundSeconds(Long roundSeconds) {
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

    public Long getNumberOfRounds() {
        return numberOfRounds;
    }

    public void setNumberOfRounds(Long numberOfRounds) {
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

}

