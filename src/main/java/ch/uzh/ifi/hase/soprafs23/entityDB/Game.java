package ch.uzh.ifi.hase.soprafs23.entityDB;

import ch.uzh.ifi.hase.soprafs23.StatePattern.*;
import ch.uzh.ifi.hase.soprafs23.constant.CategoryEnum;
import ch.uzh.ifi.hase.soprafs23.constant.Difficulty;
import ch.uzh.ifi.hase.soprafs23.constant.GameState;
import ch.uzh.ifi.hase.soprafs23.constant.RegionEnum;
import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "GAME")
public class Game {

    @Id
    private Long gameId;

    @ManyToOne
    @JoinColumn(name = "lobbyCreatorId")
    @JsonBackReference
    private GameUser lobbyCreator;

    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonBackReference
    private Set<GameUser> participants;

    @Column()
    private Date creationDate;
    @Column()
    private GameState currentState;
    @Column()
    private Long roundDuration;
    @Column()
    private Long remainingTime;
    @Column()
    private Long numberOfRounds;
    @Column()
    private Long remainingRounds;
    @Column()
    private Long remainingRoundPoints;
    @Column()
    private Long timeBetweenRounds;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "categoryStackId")
    private CategoryStack categoryStack;
    @Column()
    private Boolean openLobby;

    @Column()
    private Difficulty difficulty;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "countriesToPlay", joinColumns = @JoinColumn(name = "gameId"))
    @Column(name = "countryId")
    private Set<Long> countriesToPlayIds;
    @Column()
    private Long currentCountryId;

    @ElementCollection(targetClass = CategoryEnum.class)
    private List<CategoryEnum> availableHints;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "SELECTEDREGIONS", joinColumns = @JoinColumn(name = "gameId"))
    @Column(name = "regionEnum")
    @Enumerated(EnumType.STRING)
    @OrderColumn(name = "position")
    private List<RegionEnum> selectedRegions;

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

    public Long getNumberOfRounds() {
        return numberOfRounds;
    }

    public void setNumberOfRounds(Long numberOfRounds) {
        this.numberOfRounds = numberOfRounds;
    }

    public Boolean getOpenLobby() {
        return openLobby;
    }

    public void setOpenLobby(Boolean openLobby) {
        this.openLobby = openLobby;
    }


    public Long getRemainingRoundPoints() {
        return remainingRoundPoints;
    }

    public void setRemainingRoundPoints(Long remainingRoundPoints) {
        this.remainingRoundPoints = remainingRoundPoints;
    }


    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
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
        Set<GameUser> gamesUsers = getParticipants();
        if (gamesUsers == null) {
            throw new IllegalStateException("Game has no participants.");
        }
        for(GameUser gameUser: gamesUsers){
            gameUser.setUserPlayingState(currentState);
        }
    }

    public long getRoundDuration() {
        return roundDuration;
    }

    public void setRoundDuration(long roundDuration) {
        this.roundDuration = roundDuration;
    }

    public Set<Long> getCountriesToPlayIds() {
        return countriesToPlayIds;
    }

    public void setCountriesToPlayIds(Set<Long> countriesToPlay) {
        this.countriesToPlayIds = countriesToPlay;
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

    public Long getCurrentCountryId() {
        return currentCountryId;
    }

    public void setCurrentCountryId(Long currentCountryId) {
        this.currentCountryId = currentCountryId;
    }


    public Long getRemainingRounds() {
        return remainingRounds;
    }

    public void setRemainingRounds(Long remainingRounds) {
        this.remainingRounds = remainingRounds;
    }

    public GameUser getLobbyCreator() {
        return lobbyCreator;
    }

    public void setLobbyCreator(GameUser lobbyCreator) {
        this.lobbyCreator = lobbyCreator;
    }

    public void setRoundDuration(Long roundDuration) {
        this.roundDuration = roundDuration;
    }

    public List<CategoryEnum> getAvailableHints() {
        return availableHints;
    }

    public void setAvailableHints(List<CategoryEnum> availableHints) {
        this.availableHints = availableHints;
    }

    public List<RegionEnum> getSelectedRegions() {
        return selectedRegions;
    }

    public void setSelectedRegions(List<RegionEnum> selectedRegions) {
        this.selectedRegions = selectedRegions;
    }

    public static GameStateClass getGameStateClass(GameState gameState) {
        GameStateClass gameStateClass = null;
        switch (gameState){
            case SETUP:
                gameStateClass = new SetupStateClass();
                break;
            case GUESSING:
                gameStateClass = new GuessingStateClass();
                break;
            case SCOREBOARD:
                gameStateClass = new ScoreboardStateClass();
                break;
            case ENDED:
                gameStateClass= new EndedStateClass();
                break;
        }
        return gameStateClass;
    }

    public void removePlayer(GameUser userToRemove) {
        if (participants == null || userToRemove == null) {
            throw new IllegalArgumentException("Participants and userToRemove cannot be null.");
        }

        if (!participants.contains(userToRemove)) {
            throw new IllegalArgumentException("The userToRemove is not a participant in this game.");
        }

        participants.remove(userToRemove);
    }

    public void resetGameState() {
        if (currentState != GameState.ENDED) {
            throw new IllegalStateException("Game cannot be reset while in ENDED state.");
        }

        setCurrentState(GameState.SETUP);
        setRemainingRounds(numberOfRounds);
        setRemainingTime(roundDuration);

        for (GameUser participant : participants) {
            participant.resetScore();
        }

        if (categoryStack != null) {
            categoryStack.refillStack();
        }
        setCurrentState(GameState.GUESSING);
    }


    public void markUserAsLeft(Long userId) {
        GameUser userToMark = participants.stream()
                .filter(user -> userId.equals(user.getUserId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("User not found in the game."));

        userToMark.setHasLeft(true);
    }


    public void resetGameStateAndRemoveLeftUsers() {
        resetGameState();

        participants.removeIf(GameUser::isHasLeft);
    }


}

