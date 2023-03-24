package ch.uzh.ifi.hase.soprafs23.entity;


import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "COUNTRY")
public class Country {

    @Id
    @GeneratedValue
    @Column(name = "countryId")
    private Long countryId;



    @Column(nullable = false)
    private String name;

    @Column(nullable = true)
    private Long population;


    @Column(nullable = true)
    private String flag;


    @Embedded
    private Location location;

    @Column(nullable = true)
    private String capital;

    @Column(nullable = true, length = 10000000)
    private String outline;

    @Column(nullable = true)
    private String countryCode;

    @Column(nullable = true)
    private String region;


    public String getCountryCode() {
        return countryCode;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
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
}
