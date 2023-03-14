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


    @Column(nullable = true)
    private String flagURL;

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

    public String getFlagURL() {
        return flagURL;
    }

    public void setFlagURL(String flagURL) {
        this.flagURL = flagURL;
    }

}
