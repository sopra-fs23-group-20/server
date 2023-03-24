package ch.uzh.ifi.hase.soprafs23.entity;


import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "GAMECOUNTRY")
public class GameCountry {
    @Id
    @GeneratedValue
    @Column(name = "gameCountryId")
    private Long gameCountryId;
    private String name;
    private Long population;
    private String flag;


    @Embedded
    private Location location;

    private String capital;

    @Column(nullable = true, length = 10000000)
    private String outline;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPopulation() {
        return population;
    }

    public void setPopulation(Long population) {
        this.population = population;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public String getOutline() {
        return outline;
    }

    public void setOutline(String outline) {
        this.outline = outline;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Long getGameCountryId() {
        return gameCountryId;
    }

    public void setGameCountryId(Long gameCountryId) {
        this.gameCountryId = gameCountryId;
    }

    public static GameCountry transformToGameCountry(Country country){
        GameCountry gameCountry = new GameCountry();
        gameCountry.setName(country.getName());
        gameCountry.setPopulation(country.getPopulation());
        gameCountry.setFlag(country.getFlag());
        Location loc = new Location();
        loc.setLatitude(country.getLocation().getLatitude());
        loc.setLongitude(country.getLocation().getLongitude());
        gameCountry.setLocation(loc);
        gameCountry.setCapital(country.getCapital());
        gameCountry.setOutline(country.getOutline());
        return gameCountry;
    }

    public static Set<GameCountry> addGameCountryCollection(List<Country> countyList){
        Set<GameCountry> gameCountryList = new HashSet<>();
        for (Country country : countyList) {
            GameCountry gameCountry = new GameCountry();
            gameCountry.setName(country.getName());
            gameCountry.setPopulation(country.getPopulation());
            gameCountry.setFlag(country.getFlag());
            Location loc = new Location();
            loc.setLatitude(country.getLocation().getLatitude());
            loc.setLongitude(country.getLocation().getLongitude());
            gameCountry.setLocation(loc);
            gameCountry.setCapital(country.getCapital());
            gameCountry.setOutline(country.getOutline());
            gameCountryList.add(gameCountry);
        }
        return gameCountryList;
    }

}
