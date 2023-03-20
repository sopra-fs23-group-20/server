package ch.uzh.ifi.hase.soprafs23.entity;

import ch.uzh.ifi.hase.soprafs23.constant.CategoryEnum;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Entity
@Table(name = "CATEGORY")
public class Category {

    @Id
    @GeneratedValue
    private Long categoryId;

    private Long gameId;


    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private CategoryEnum type;

    @Column(nullable = true)
    private String capital;

    @Column(nullable = true)
    private String flag;

    @Column(nullable = true)
    private long population;

    @ElementCollection
    @CollectionTable(name = "location", joinColumns = @JoinColumn(name = "categoryId"))
    @Column(nullable = true)
    private List<Double> location;

    @Column(nullable = true)
    private String outline;


    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public CategoryEnum getType() {
        return type;
    }

    public void setType(CategoryEnum type) {
        this.type = type;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public long getPopulation() {
        return population;
    }

    public void setPopulation(long population) {
        this.population = population;
    }

    public List<Double> getLocation() {
        return location;
    }

    public void setLocation(List<Double> location) {
        this.location = location;
    }

    public String getOutline() {
        return outline;
    }

    public void setOutline(String outline) {
        this.outline = outline;
    }

    public static Category transformToCategory(CategoryEnum type, Country country){
        Category category = new Category();
        category.setType(type);
        switch (type){

            case POPULATION:
                category.setPopulation(country.getPopulation());
                return category;
            case OUTLINE:
                category.setOutline(country.getOutline());
                return category;
            case FLAG:
                category.setFlag(country.getFlag());
                return category;
            case LOCATION:
                double latitude = country.getLatitude();
                double longitude = country.getLongitude();
                List<Double> location = new ArrayList<>();
                location.add(latitude);
                location.add(longitude);
                category.setLocation(location);
                return category;
            case CAPITAL:
                category.setPopulation(country.getPopulation());
                return category;

            default:
                return null;
        }
    }
}
