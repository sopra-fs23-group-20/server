package ch.uzh.ifi.hase.soprafs23.entityDB;


import ch.uzh.ifi.hase.soprafs23.constant.RegionEnum;
import ch.uzh.ifi.hase.soprafs23.entityOther.Location;

import javax.persistence.*;
import javax.swing.plaf.synth.Region;

@Entity
@Table(name = "COUNTRY")
public class Country {

    @Id
    @GeneratedValue
    @Column(name = "countryId")
    private Long countryId;



    @Column(nullable = false)
    private String name;

    @Column()
    private Long population;


    @Column()
    private String flag;


    @Embedded
    private Location location;

    @Column()
    private String capital;

    @OneToOne
    private Outline outline;

    @Column()
    private String countryCode;

    @Column()
    private RegionEnum region;


    public String getCountryCode() {
        return countryCode;
    }

    public RegionEnum getRegion() {
        return region;
    }

    public void setRegion(RegionEnum region) {
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

    public Outline getOutline() {
        return outline;
    }

    public void setOutline(Outline outline) {
        this.outline = outline;
    }
}
