package ch.uzh.ifi.hase.soprafs23.entity;


import javax.persistence.*;

@Entity
@Table(name = "COUNTRY")
public class Country {
    private static final long serialVersionUID = 2L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;



    @Column(nullable = false)
    private Long population;


    @Column(nullable = false)
    private String flag;

    @Column(nullable = false)
    private Double latitude;
    private Double longitude;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String countryName) {
        this.name = countryName;
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

    public void setFlag(String flagURL) {
        this.flag = flagURL;
    }

}
