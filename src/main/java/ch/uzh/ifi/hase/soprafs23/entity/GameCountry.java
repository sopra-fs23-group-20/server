package ch.uzh.ifi.hase.soprafs23.entity;


import javax.persistence.Embeddable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Embeddable
public class GameCountry {
    private String name;
    private Long population;
    private String flag;
    private Double latitude;
    private Double longitude;

    private String capital;

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

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
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

    public static Set<GameCountry> addGameCountryCollection(List<Country> countyList){
        Set<GameCountry> gameCountryList = new HashSet<>();
        for (Country country : countyList) {
            GameCountry gameCountry = new GameCountry();
            gameCountry.setName(country.getName());
            gameCountry.setPopulation(country.getPopulation());
            gameCountry.setFlag(country.getFlag());
            gameCountry.setLatitude(country.getLatitude());
            gameCountry.setLongitude(country.getLongitude());
            gameCountry.setCapital(country.getCapital());
            gameCountry.setOutline(country.getOutline());
            gameCountryList.add(gameCountry);
        }
        return gameCountryList;
    }

}
