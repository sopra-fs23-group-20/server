package ch.uzh.ifi.hase.soprafs23.entityDB;

import ch.uzh.ifi.hase.soprafs23.constant.CategoryEnum;
import ch.uzh.ifi.hase.soprafs23.constant.GameState;
import ch.uzh.ifi.hase.soprafs23.constant.RegionEnum;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "GAME")
public class Game {

    @Id
    @GeneratedValue
    private Long gameId;


    private Long lobbyCreatorUserId;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "participants", joinColumns = @JoinColumn(name = "gameId"))
    private Set<GameUser> participants;

    @Column()
    private Date creationDate;
    @Column()
    private GameState currentState;
    @Column()
    private long gameEndingCriteria;
    @Column()
    private long roundDuration;
    @Column()
    private RegionEnum region;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "countriesToPlay", joinColumns = @JoinColumn(name = "gameId"))
    @Column(name = "countryId")
    private Set<Long> countriesToPlayIds;
    @Column()
    private long currentRound;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "categoryStackId")
    private CategoryStack categoryStack;
    @Column()
    private Long remainingTime;

    @Column(nullable = true)
    private Long totalRoundTime;

    private Long currentCountryId;

    private Long remainingRoundPoints;

    private int remainingRounds = 0;

    private int roundSeconds;
    private Boolean randomizedHints;
    private Boolean allCountries;
    private int numberOfRounds;
    private Boolean openLobby;

    public int getRoundSeconds() {
        return roundSeconds;
    }

    public void setRoundSeconds(int roundSeconds) {
        this.roundSeconds = roundSeconds;
    }

    public Boolean getRandomizedHints() {
        return randomizedHints;
    }

    public void setRandomizedHints(Boolean randomizedHints) {
        this.randomizedHints = randomizedHints;
    }

    public Boolean getAllCountries() {
        return allCountries;
    }

    public void setAllCountries(Boolean allCountries) {
        this.allCountries = allCountries;
    }

    public int getNumberOfRounds() {
        return numberOfRounds;
    }

    public void setNumberOfRounds(int numberOfRounds) {
        this.numberOfRounds = numberOfRounds;
    }

    public Boolean getOpenLobby() {
        return openLobby;
    }

    public void setOpenLobby(Boolean openLobby) {
        this.openLobby = openLobby;
    }

    public List<CategoryEnum> getAvailableHints() {
        return availableHints;
    }

    public void setAvailableHints(List<CategoryEnum> availableHints) {
        this.availableHints = availableHints;
    }

    @ElementCollection(targetClass = CategoryEnum.class)
    private List<CategoryEnum> availableHints;

    public Long getRemainingRoundPoints() {
        return remainingRoundPoints;
    }

    public void setRemainingRoundPoints(Long remainingRoundPoints) {
        this.remainingRoundPoints = remainingRoundPoints;
    }

    public Long getTotalRoundTime() {
        return totalRoundTime;
    }

    public void setTotalRoundTime(Long totalRoundTime) {
        this.totalRoundTime = totalRoundTime;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public Long getLobbyCreatorUserId() {
        return lobbyCreatorUserId;
    }

    public void setLobbyCreatorUserId(Long lobbyCreatorUserId) {
        this.lobbyCreatorUserId = lobbyCreatorUserId;
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
        for(GameUser gameUser: gamesUsers){
            gameUser.setCurrentState(currentState);
        }
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

    public Set<Long> getCountriesToPlayIds() {
        return countriesToPlayIds;
    }

    public void setCountriesToPlayIds(Set<Long> countriesToPlay) {
        this.countriesToPlayIds = countriesToPlay;
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

    public Long getCurrentCountryId() {
        return currentCountryId;
    }

    public void setCurrentCountryId(Long currentCountryId) {
        this.currentCountryId = currentCountryId;
    }


    public int getRemainingRounds() {
        return remainingRounds;
    }

    public void setRemainingRounds(int remainingRounds) {
        this.remainingRounds = remainingRounds;
    }
}

