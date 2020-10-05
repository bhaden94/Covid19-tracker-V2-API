package com.covid19trackerv2.services;

import com.covid19trackerv2.model.country.Country;
import com.covid19trackerv2.model.country.CountryDoc;
import com.covid19trackerv2.model.state.StateDoc;
import com.covid19trackerv2.model.state.UsState;
import com.covid19trackerv2.repository.CountryRepository;
import com.covid19trackerv2.repository.UsStateRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class CovidDataServices {

    private final String US_STATE_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_daily_reports_us/";
    private final int STATE_START_YEAR = 2020;
    private final int STATE_START_MONTH = 4;
    private final int STATE_START_DAY = 12;

    private final String COUNTRY_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_daily_reports/";
    private final int COUNTRY_START_YEAR = 2020;
    private final int COUNTRY_START_MONTH = 1;
    private final int COUNTRY_START_DAY = 22;

    @Autowired
    private UsStateRepository statesRepo;

    @Autowired
    private CountryRepository countryRepo;

    // run at 0615 UTC everyday in AsyncDBLoad class
    public void fetchDailyStateStats() throws IOException, InterruptedException {
        String formattedDate = getFormattedDate();
        Iterable<CSVRecord> records = getRecords(formattedDate, US_STATE_URL);
        List<UsState> states = createStatesList(records);
        StateDoc doc = new StateDoc();

        doc.setDate(LocalDate.now(ZoneId.of("UTC")));
        doc.setStates(states);
        this.statesRepo.save(doc);
    }

    // run at 0615 UTC everyday in AsyncDBLoad class
    public void fetchDailyCountryStats() throws IOException, InterruptedException {
        String formattedDate = getFormattedDate();
        Iterable<CSVRecord> records = getRecords(formattedDate, COUNTRY_URL);
        List<Country> countries = createCountryList(records);
        CountryDoc doc = new CountryDoc();

        doc.setDate(LocalDate.now(ZoneId.of("UTC")));
        doc.setCountries(countries);
        this.countryRepo.save(doc);
    }

    // this method will be loaded concurrently in a thread at startup from AsyncDBLoad class
    public void populateDbWithStateData() throws IOException, InterruptedException {
        System.out.println("Start state DB population");
        LocalDate startDate = LocalDate.of(STATE_START_YEAR, STATE_START_MONTH, STATE_START_DAY);
        LocalDate today = LocalDate.now(ZoneId.of("UTC"));

        long daysBetween = DAYS.between(startDate, today);
        LocalDate current = startDate;
        for (long i = daysBetween; i >= 2; i--) {
            if (this.statesRepo.findByDate(current).isEmpty()) {
                String formattedDate = getFormattedDate(current);
                Iterable<CSVRecord> records = getRecords(formattedDate, US_STATE_URL);
                List<UsState> states = createStatesList(records);
                StateDoc doc = new StateDoc();
                doc.setDate(current);
                doc.setStates(states);
                this.statesRepo.save(doc);
                current = current.plusDays(1);
            }
        }
        System.out.println("exit state DB population");
    }

    // this method will be loaded concurrently in a thread at startup from AsyncDBLoad class
    public void populateDBWithCountryData() throws IOException, InterruptedException {
        System.out.println("Start country DB population");
        LocalDate startDate = LocalDate.of(COUNTRY_START_YEAR, COUNTRY_START_MONTH, COUNTRY_START_DAY);
        LocalDate today = LocalDate.now(ZoneId.of("UTC"));

        long daysBetween = DAYS.between(startDate, today);
        LocalDate current = startDate;
        for (long i = daysBetween; i >= 2; i--) {
            if (this.countryRepo.findByDate(current).isEmpty()) {
                String formattedDate = getFormattedDate(current);
                Iterable<CSVRecord> records = getRecords(formattedDate, COUNTRY_URL);
                List<Country> countries = createCountryList(records);
                CountryDoc doc = new CountryDoc();
                doc.setDate(current);
                doc.setCountries(countries);
                this.countryRepo.save(doc);
                current = current.plusDays(1);
            }
        }
        System.out.println("exit country DB population");
    }

    /* ---- Helper methods ---- */

    private Iterable<CSVRecord> getRecords(String formattedDate, String url) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url + formattedDate + ".csv"))
                .build();

        HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());

        StringReader csvBodyReader = new StringReader(res.body());
        return CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .parse(csvBodyReader);
    }

    private List<UsState> createStatesList(Iterable<CSVRecord> records) {
        List<UsState> states = new ArrayList<>();
        for (CSVRecord record : records) {
            UsState state = new UsState();
            state.setState(record.get("Province_State").toLowerCase());
            state.setConfirmed(getLongValueFromRecord(record.get("Confirmed")));
            state.setDeaths(getLongValueFromRecord(record.get("Deaths")));
            state.setRecovered(getLongValueFromRecord(record.get("Recovered")));
            state.setActive(getLongValueFromRecord(record.get("Active")));
            state.setIncidentRate(getDoubleValueFromRecord(record.get("Incident_Rate")));
            state.setMortalityRate(getDoubleValueFromRecord(record.get("Mortality_Rate")));
            states.add(state);
        }
        return states;
    }

    private List<Country> createCountryList(Iterable<CSVRecord> records) {
        Map<String, Country> countries = new HashMap<>();
        for (CSVRecord record : records) {
            Country country = new Country();
            if (record.isMapped("Country/Region")) {
                country.setCountry(record.get("Country/Region").toLowerCase());
            } else {
                country.setCountry(record.get("Country_Region").toLowerCase());
            }
            // values that are in all records
            country.setConfirmed(getLongValueFromRecord(record.get("Confirmed")));
            country.setDeaths(getLongValueFromRecord(record.get("Deaths")));
            country.setRecovered(getLongValueFromRecord(record.get("Recovered")));

            // values that are not in all records
            if (record.isMapped("Active")) {
                country.setActive(getLongValueFromRecord(record.get("Active")));
            } else {
                country.setActive(0L);
            }
            if (record.isMapped("Incidence_Rate")) {
                country.setIncidentRate(getDoubleValueFromRecord(record.get("Incidence_Rate")));
            } else {
                country.setIncidentRate(0.0);
            }
            if (record.isMapped("Case-Fatality_Ratio")) {
                country.setMortalityRate(getDoubleValueFromRecord(record.get("Case-Fatality_Ratio")));
            } else {
                country.setMortalityRate(0.0);
            }

            // if our country is already there then add the values up
            if (countries.containsKey(country.getCountry())) {
                Country existing = countries.get(country.getCountry());
                existing.setConfirmed(existing.getConfirmed() + country.getConfirmed());
                existing.setDeaths(existing.getDeaths() + country.getDeaths());
                existing.setRecovered(existing.getRecovered() + country.getRecovered());
                existing.setActive(existing.getActive() + country.getActive());
                // carry over the incident rate and mortality rate as it is calculated already
                existing.setIncidentRate(country.getIncidentRate());
                existing.setMortalityRate(country.getMortalityRate());
                countries.put(country.getCountry(), existing);
            } else {
                countries.put(country.getCountry(), country);
            }
        }
        // return the values in the Map as an ArrayList
        return new ArrayList<>(countries.values());
    }

    private long getLongValueFromRecord(String numToParse) {
        long num = 0;
        int dotIndex = numToParse.indexOf('.');
        if (dotIndex != -1) {
            numToParse = numToParse.substring(0, dotIndex);
        }
        try {
            num = Long.parseLong(numToParse);
        } catch (NumberFormatException ignored) {
        }
        return num;
    }

    private double getDoubleValueFromRecord(String numToParse) {
        double num = 0.0;
        try {
            num = Double.parseDouble(numToParse);
        } catch (NumberFormatException ignored) {
        }
        return num;
    }

    private String getFormattedDate() {
        LocalDate date = LocalDate.now(ZoneId.of("UTC"));
        return date.format(DateTimeFormatter.ofPattern("MM-dd-yyyy"));
    }

    private String getFormattedDate(LocalDate current) {
        return current.format(DateTimeFormatter.ofPattern("MM-dd-yyyy"));
    }

}
