package ch.uzh.ifi.hase.soprafs23.entity;


import javax.persistence.*;

@Entity
@Table(name = "COUNTRY")
public class Country {

    @Id
    @GeneratedValue
    @Column(name = "countryId")
    private Long countryId;


    @ManyToOne
    @JoinColumn(name = "gameId")
    private Game game;





    @Column(nullable = false)
    private String name;

    @Column(nullable = true)
    private Long population;


    @Column(nullable = true)
    private String flag;

    @Column(nullable = true)
    private Double latitude;
    @Column(nullable = true)
    private Double longitude;

    @Column(nullable = true)
    private String capital;

    @Column(nullable = true)
    private String outline;


    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
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
}
