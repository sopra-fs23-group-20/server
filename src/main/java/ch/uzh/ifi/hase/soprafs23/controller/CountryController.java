package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.entity.Country;
import ch.uzh.ifi.hase.soprafs23.rest.dto.CountryGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.CountryService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
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
        countryService.setCountriesWithAPI();
        List< Country> countries = countryService.getAllCountries();
        List<CountryGetDTO> countryGetDTOS = new ArrayList<>();
        for (Country country : countries){
            countryGetDTOS.add(DTOMapper.INSTANCE.convertEntityToCountryGetDTO(country));
        }
        countryGetDTOS.sort((o1, o2) -> o1.getName().compareTo(o2.getName()));
        return countryGetDTOS;
    }


    @GetMapping("/countries")
    @ResponseStatus(HttpStatus.OK)
    public List<CountryGetDTO> getCountries() {
        List< Country> countries = countryService.getAllCountries();
        List<CountryGetDTO> countryGetDTOS = new ArrayList<>();
        for (Country country : countries){
            countryGetDTOS.add(DTOMapper.INSTANCE.convertEntityToCountryGetDTO(country));
        }
        return countryGetDTOS;
    }

}
