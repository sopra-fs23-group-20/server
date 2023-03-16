package ch.uzh.ifi.hase.soprafs23.service;


import ch.uzh.ifi.hase.soprafs23.entity.Country;
import ch.uzh.ifi.hase.soprafs23.repository.CountryRepository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

@Service
@Transactional
public class CountryService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final CountryRepository countryRepository;


    @Autowired
    public CountryService(@Qualifier("countryRepository") CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public List<Country> getAllCountries() {
        return this.countryRepository.findAll();
    }

    public Country getRandomCountry(){
        List<Country> countries = getAllCountries();
        int randomIndex = (int) (Math.random() * countries.size());
        return countries.get(randomIndex);
    }

    public void setCountriesWithAPI() {

        try {
            // Connect to the REST API URL and read the JSON response
            URL url = new URL("https://restcountries.com/v3.1/all");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            InputStream stream = connection.getInputStream();
            Scanner scanner = new Scanner(stream);
            StringBuilder responseBuilder = new StringBuilder();
            while (scanner.hasNext()) {
                responseBuilder.append(scanner.nextLine());
            }
            String response = responseBuilder.toString();

            // Parse the JSON response and extract the name and population of each country
            JSONParser parser = new JSONParser();
            JSONArray countries = (JSONArray) parser.parse(response);
            for (Object obj : countries) {
                JSONObject country = (JSONObject) obj;
                String name = (String) ((JSONObject) country.get("name")).get("common");
                long population = (long) country.get("population");
                String flag = (String) ((JSONObject) country.get("flags")).get("svg");
                List<Double> latlng = (List<Double>) country.get("latlng");


                Country newCountry = new Country();
                newCountry.setName(name);
                newCountry.setPopulation(population);
                newCountry.setFlag(flag);
                newCountry.setLatitude(latlng.get(0));
                newCountry.setLongitude(latlng.get(1));
                countryRepository.save(newCountry);
            }
            countryRepository.flush();
        }
        catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public void setCountriesWithFile() {

        JSONParser parser = new JSONParser();

        try {
            // Read the JSON file
            JSONArray countries = (JSONArray) parser.parse(new FileReader("src/main/resources/countries.json"));

            // Extract the name and population of each country
            for (Object obj : countries) {
                JSONObject country = (JSONObject) obj;
                String name = (String) ((JSONObject) country.get("name")).get("common");
                long population = (long) country.get("population");
                String flag = (String) ((JSONObject) country.get("flags")).get("svg");
                List<Double> latlng = (List<Double>) country.get("latlng");


                Country newCountry = new Country();
                newCountry.setName(name);
                newCountry.setPopulation(population);
                newCountry.setFlag(flag);
                newCountry.setLatitude(latlng.get(0));
                newCountry.setLongitude(latlng.get(1));
                countryRepository.save(newCountry);
            }
            countryRepository.flush();
        }
        catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

}
