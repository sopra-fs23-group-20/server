package ch.uzh.ifi.hase.soprafs23.rest.dto;

import ch.uzh.ifi.hase.soprafs23.constant.Difficulty;
import ch.uzh.ifi.hase.soprafs23.constant.RegionEnum;
import ch.uzh.ifi.hase.soprafs23.entityDB.CategoryStack;

import java.util.List;

    public class GamePutDTO {

        private Long userId;

        private String lobbyCreatorUserId;
        private Long roundDuration;
        private Long numberOfRounds;
        private CategoryStack categoryStack;
        private List<RegionEnum> selectedRegions;
        private boolean openLobby;
        private Difficulty difficulty;

        public Difficulty getDifficulty() {
            return difficulty;
        }

        public void setDifficulty(Difficulty difficulty) {
            this.difficulty = difficulty;
        }

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

        public CategoryStack getCategoryStack() {
            return categoryStack;
        }

        public void setCategoryStack(CategoryStack categoryStack) {
            this.categoryStack = categoryStack;
        }

        public List<RegionEnum> getSelectedRegions() {
            return selectedRegions;
        }

        public void setSelectedRegions(List<RegionEnum> selectedRegions) {
            this.selectedRegions = selectedRegions;
        }

        public boolean isOpenLobby() {
            return openLobby;
        }

        public void setOpenLobby(boolean openLobby) {
            this.openLobby = openLobby;
        }
        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }
    }



