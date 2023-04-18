package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.constant.RegionEnum;
import ch.uzh.ifi.hase.soprafs23.entityDB.Country;
import ch.uzh.ifi.hase.soprafs23.rest.dto.CountryGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.CountryService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RestController
public class CountryController {
    private final CountryService countryService;

    CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @PostMapping("/countries")
    @ResponseStatus(HttpStatus.CREATED)
    public List<CountryGetDTO> createCountry() {
        countryService.setAllCountries();
        return getCountryGetDTOS();
    }

    private List<CountryGetDTO> getCountryGetDTOS() {
        List<Country> countries = countryService.getAllCountries();
        List<CountryGetDTO> countryGetDTOS = new ArrayList<>();
        for (Country country : countries){
            countryGetDTOS.add(DTOMapper.INSTANCE.convertEntityToCountryGetDTO(country));
        }
        countryGetDTOS.sort(Comparator.comparing(CountryGetDTO::getName));
        return countryGetDTOS;
    }


    @GetMapping("/countries")
    @ResponseStatus(HttpStatus.OK)
    public List<CountryGetDTO> getCountries() {
        return getCountryGetDTOS();
    }

    @GetMapping("/countries/europe")
    @ResponseStatus(HttpStatus.OK)
    public List<CountryGetDTO> getCountriesEurope() {
        return getCountryGetDTOSByRegion(regionEnumToString(RegionEnum.EUROPE));
    }

    @GetMapping("/countries/asia")
    @ResponseStatus(HttpStatus.OK)
    public List<CountryGetDTO> getCountriesAsia() {
        return getCountryGetDTOSByRegion(regionEnumToString(RegionEnum.ASIA));
    }

    @GetMapping("/countries/america")
    @ResponseStatus(HttpStatus.OK)
    public List<CountryGetDTO> getCountriesAmerica() {
        return getCountryGetDTOSByRegion(regionEnumToString(RegionEnum.AMERICA));
    }

    @GetMapping("/countries/oceania")
    @ResponseStatus(HttpStatus.OK)
    public List<CountryGetDTO> getCountriesOceania() {
        return getCountryGetDTOSByRegion(regionEnumToString(RegionEnum.OCEANIA));
    }

    @GetMapping("/countries/africa")
    @ResponseStatus(HttpStatus.OK)
    public List<CountryGetDTO> getCountriesAfrica() {
        return getCountryGetDTOSByRegion(regionEnumToString(RegionEnum.AFRICA));
    }

    @GetMapping("/countries/antarctica")
    @ResponseStatus(HttpStatus.OK)
    public List<CountryGetDTO> getCountriesAntarctica() {
        return getCountryGetDTOSByRegion(regionEnumToString(RegionEnum.ANTARCTIC));
    }

    private String regionEnumToString(RegionEnum regionEnum) {
        switch (regionEnum) {
            case AFRICA:
                return "Africa";
            case ASIA:
                return "Asia";
            case EUROPE:
                return "Europe";
            case AMERICA:
                return "Americas";
            case OCEANIA:
                return "Oceania";
            case ANTARCTIC:
                return "Antarctic";
            default:
                throw new IllegalArgumentException("Invalid region enum: " + regionEnum);
        }
    }



    private List<CountryGetDTO> getCountryGetDTOSByRegion(String region) {
        List<Country> countries = countryService.getCountriesByContinent(region);
        List<CountryGetDTO> countryGetDTOS = new ArrayList<>();
        for (Country country : countries) {
            countryGetDTOS.add(DTOMapper.INSTANCE.convertEntityToCountryGetDTO(country));
        }
        countryGetDTOS.sort(Comparator.comparing(CountryGetDTO::getName));
        return countryGetDTOS;
    }

}
