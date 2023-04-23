package ch.uzh.ifi.hase.soprafs23.service;


import ch.uzh.ifi.hase.soprafs23.constant.Difficulty;
import ch.uzh.ifi.hase.soprafs23.constant.RegionEnum;
import ch.uzh.ifi.hase.soprafs23.entityDB.Country;
import ch.uzh.ifi.hase.soprafs23.entityDB.Outline;
import ch.uzh.ifi.hase.soprafs23.entityOther.Location;
import ch.uzh.ifi.hase.soprafs23.repository.CountryRepository;
import ch.uzh.ifi.hase.soprafs23.repository.OutlineRepository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.plaf.synth.Region;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class CountryService {

    private final CountryRepository countryRepository;
    private final OutlineRepository outlineRepository;

    @Autowired
    public CountryService(@Qualifier("countryRepository") CountryRepository countryRepository, @Qualifier("outlineRepository") OutlineRepository outlineRepository) {
        this.countryRepository = countryRepository;
        this.outlineRepository = outlineRepository;
        this.setAllCountries();
    }

    public List<Country> getAllCountries() {
        return this.countryRepository.findAll();
    }


    private String getCountryOutline(String countryCode, JSONArray outlines) {

        for (Object featureObj : outlines) {
            JSONObject feature = (JSONObject) featureObj;
            JSONObject properties = (JSONObject) feature.get("properties");
            if (countryCode.equals(properties.get("ISO_A3"))) {
                return featureObj.toString();
            }
        }
        return null;
    }

    public void setAllCountries() {
        if (countryRepository.getAllCountryIds().size() == 0) {
            JSONArray countries;

            /*
            try{
                countries = fetchCountriesAPI(countries);
            }
            catch (IOException | ParseException e) {
            }*/

            {
                JSONParser parser = new JSONParser();
                try {
                    countries = (JSONArray) parser.parse(new FileReader("src/main/resources/countries.json"));
                    System.out.println("Set countries with File");
                }
                catch (IOException | ParseException ex) {
                    throw new RuntimeException(ex);
                }
            }

            try {
                JSONParser parser = new JSONParser();
                JSONObject outlines_Object = (JSONObject) parser.parse(new FileReader("src/main/resources/outlines.json"));
                JSONArray outlines = (JSONArray) outlines_Object.get("features");

                for (Object obj : countries) {
                    try {
                        JSONObject country = (JSONObject) obj;
                        String name = (String) ((JSONObject) country.get("name")).get("common");
                        long population = (long) country.get("population");
                        String flag = (String) ((JSONObject) country.get("flags")).get("svg");
                        String region = (String) country.get("region");
                        String outline;
                        String countryCode;
                        String capital;
                        List<Double> latlngList;
                        latlngList = (List<Double>) ((JSONObject) country.get("capitalInfo")).get("latlng");
                        Location location = new Location();
                        location.setLatitude(latlngList.get(0));
                        location.setLongitude(latlngList.get(1));

                        capital = (String) ((JSONArray) country.get("capital")).get(0);
                        countryCode = (String) country.get("cca3");
                        outline = getCountryOutline(countryCode, outlines);


                        Country newCountry = new Country();
                        newCountry.setName(name);
                        newCountry.setRegion(stringToRegionEnum(region));
                        newCountry.setCountryCode(countryCode);
                        newCountry.setCapital(capital);
                        newCountry.setPopulation(population);
                        newCountry.setFlag(flag);
                        newCountry.setLocation(location);

                        Outline outline1 = new Outline();
                        outline1.setOutline(outline);
                        outlineRepository.saveAndFlush(outline1);
                        newCountry.setOutline(outline1);

                        countryRepository.save(newCountry);
                    }
                    catch (Exception ignored) {
                    }
                    countryRepository.flush();
                }
            }

            catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }
    }



    public Long getMinPopulationByDifficulty(Difficulty difficulty) {
        Long minPopulation;
        switch (difficulty) {
            case EASY:
                minPopulation = calculateTopPopulation(1.0 / 3);
                break;
            case MEDIUM:
                minPopulation = calculateTopPopulation(2.0 / 3);
                break;
            case HARD:
            default:
                minPopulation = 0L;
                break;
        }
        return minPopulation;
    }

    public Long calculateTopPopulation(double ratio) {
        List<Country> countries = countryRepository.findAll(Sort.by(Sort.Direction.DESC, "population"));
        int index = (int) (countries.size() * ratio) - 1;
        return countries.get(index).getPopulation();
    }

    private RegionEnum stringToRegionEnum(String region) {
        switch (region) {
            case "Africa":
                return RegionEnum.AFRICA;
            case "Asia":
                return RegionEnum.ASIA;
            case "Europe":
                return RegionEnum.EUROPE;
            case "Americas":
                return RegionEnum.AMERICA;
            case "Oceania":
                return RegionEnum.OCEANIA;
            case "Antarctic":
                return RegionEnum.ANTARCTICA;
            default:
                throw new IllegalArgumentException("Invalid region string: " + region);
        }
    }

    public List<Country> getCountriesByContinent(String continent) {
        List<Country> allCountries = getAllCountries();
        return allCountries.stream()
                .filter(country -> continent.equals(country.getRegion()))
                .collect(Collectors.toList());
    }

}
