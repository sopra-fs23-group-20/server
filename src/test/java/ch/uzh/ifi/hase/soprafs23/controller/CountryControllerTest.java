package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.constant.RegionEnum;
import ch.uzh.ifi.hase.soprafs23.entityDB.Country;
import ch.uzh.ifi.hase.soprafs23.rest.dto.CountryGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.CountryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.*;

@WebMvcTest(CountryController.class)
public class CountryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CountryService countryService;


    @Test
    public void getAllCountries_noCountries_returnEmptyList() throws Exception {
        given(countryService.getAllCountries()).willReturn(new ArrayList<>());

        mockMvc.perform(get("/countries")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void getCountries_allCountries_returnList() throws Exception {
        List<Country> countries = createSampleCountries();
        List<CountryGetDTO> countryGetDTOs = countries.stream()
                .map(DTOMapper.INSTANCE::convertEntityToCountryGetDTO)
                .collect(Collectors.toList());

        given(countryService.getAllCountries()).willReturn(countries);

        mockMvc.perform(get("/countries")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(countries.size())));
    }

    @Test
    public void getCountriesEurope_returnsList() throws Exception {
        testGetCountriesByRegion("/countries/europe", RegionEnum.EUROPE);
    }

    @Test
    public void getCountriesAsia_returnsList() throws Exception {
        testGetCountriesByRegion("/countries/asia", RegionEnum.ASIA);
    }

    @Test
    public void getCountriesAmerica_returnsList() throws Exception {
        testGetCountriesByRegion("/countries/america", RegionEnum.AMERICA);
    }

    @Test
    public void getCountriesOceania_returnsList() throws Exception {
        testGetCountriesByRegion("/countries/oceania", RegionEnum.OCEANIA);
    }

    @Test
    public void getCountriesAfrica_returnsList() throws Exception {
        testGetCountriesByRegion("/countries/africa", RegionEnum.AFRICA);
    }

    @Test
    public void getCountriesAntarctica_returnsList() throws Exception {
        testGetCountriesByRegion("/countries/antarctica", RegionEnum.ANTARCTICA);
    }


    @Test
    private void testGetCountriesByRegion(String url, RegionEnum region) throws Exception {
        List<Country> countries = createSampleCountries();
        List<CountryGetDTO> countryGetDTOs = countries.stream()
                .map(DTOMapper.INSTANCE::convertEntityToCountryGetDTO)
                .collect(Collectors.toList());

        given(countryService.getCountriesByContinent(anyString())).willReturn(countries);

        mockMvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(countries.size())));
    }


    private List<Country> createSampleCountries() {
        List<Country> countries = new ArrayList<>();

        Country country1 = new Country();
        country1.setCountryId(1L);
        country1.setName("Country 1");
        country1.setRegion(RegionEnum.EUROPE);

        Country country2 = new Country();
        country2.setCountryId(2L);
        country2.setName("Country 2");
        country2.setRegion(RegionEnum.AFRICA);

        countries.add(country1);
        countries.add(country2);

        return countries;
    }


}
