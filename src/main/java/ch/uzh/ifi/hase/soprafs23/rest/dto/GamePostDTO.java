package ch.uzh.ifi.hase.soprafs23.rest.dto;

import ch.uzh.ifi.hase.soprafs23.constant.CategoryEnum;
import ch.uzh.ifi.hase.soprafs23.constant.RegionEnum;

import java.util.List;

public class GamePostDTO {
    private String lobbyCreatorUserId;
    private Long roundDuration;
    private Long numberOfRounds;
    private List<CategoryEnum> categoriesSelected;
    private List<RegionEnum> regionsSelected;
    private boolean randomizedCategories;
    private boolean openLobby;


    public String getLobbyCreatorUserId() {
        return lobbyCreatorUserId;
    }

    public void setLobbyCreatorUserId(String lobbyCreatorUserId) {
        this.lobbyCreatorUserId = lobbyCreatorUserId;
    }

    public Long getRoundDuration() {
        return roundDuration;
    }

    public void setRoundDuration(Long roundDuration) {
        this.roundDuration = roundDuration;
    }

    public Long getNumberOfRounds() {
        return numberOfRounds;
    }

    public void setNumberOfRounds(Long numberOfRounds) {
        this.numberOfRounds = numberOfRounds;
    }

    public List<CategoryEnum> getCategoriesSelected() {
        return categoriesSelected;
    }

    public void setCategoriesSelected(List<CategoryEnum> categoriesSelected) {
        this.categoriesSelected = categoriesSelected;
    }

    public List<RegionEnum> getRegionsSelected() {
        return regionsSelected;
    }

    public void setRegionsSelected(List<RegionEnum> regionsSelected) {
        this.regionsSelected = regionsSelected;
    }

    public boolean isRandomizedCategories() {
        return randomizedCategories;
    }

    public void setRandomizedCategories(boolean randomizedCategories) {
        this.randomizedCategories = randomizedCategories;
    }

    public boolean isOpenLobby() {
        return openLobby;
    }

    public void setOpenLobby(boolean openLobby) {
        this.openLobby = openLobby;
    }
}

