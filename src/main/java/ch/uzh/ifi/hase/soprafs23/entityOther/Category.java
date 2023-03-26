package ch.uzh.ifi.hase.soprafs23.entityOther;

import ch.uzh.ifi.hase.soprafs23.constant.CategoryEnum;
import ch.uzh.ifi.hase.soprafs23.entityDB.Country;

import javax.persistence.*;
import java.io.Serializable;



public class Category {

    private CategoryEnum type;

    private String capital;
    private String flag;
    private long population;

    private Location location;

    private String outline;

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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
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
                Location location = country.getLocation();
                category.setLocation(location);
                return category;

            case CAPITAL:
                category.setCapital(country.getCapital());
                return category;

            default:
                return null;
        }
    }
}
