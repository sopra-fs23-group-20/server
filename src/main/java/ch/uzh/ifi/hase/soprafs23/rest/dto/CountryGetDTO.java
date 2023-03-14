package ch.uzh.ifi.hase.soprafs23.rest.dto;

public class CountryGetDTO {
    private String name;
    private Long population;

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
}
