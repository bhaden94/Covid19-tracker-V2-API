package com.covid19trackerv2.controller;

import com.covid19trackerv2.model.country.CountryDoc;
import com.covid19trackerv2.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/api/country")
public class CountryController {

    @Autowired
    private Environment environment;

    @Autowired
    private CountryRepository countryRepo;

    @GetMapping("/all")
    public ResponseEntity<List<CountryDoc>> getCountries() {
        return ResponseEntity.ok().body(this.countryRepo.findAll());
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<CountryDoc>> getCountriesPageable(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok().body(this.countryRepo.findAll(PageRequest.of(page, size)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getCountryById(@PathVariable String id) {
        Optional<CountryDoc> country = this.countryRepo.findById(id);
        return country.<ResponseEntity<Object>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.ok()
                        .body("Country document not found with id " + id));
    }

    @GetMapping("/date")
    public ResponseEntity<Object> getStateByDate(
            @RequestParam int year,
            @RequestParam int month,
            @RequestParam int day) {
        LocalDate date = LocalDate.of(year, month, day);
        Optional<CountryDoc> state = this.countryRepo.findByDate(date);
        return state.<ResponseEntity<Object>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.ok()
                        .body("Country document not found with date " + date.toString()));
    }

    @GetMapping("")
    public ResponseEntity<List<CountryDoc>> getCountryByName(@RequestParam String name) {
        List<CountryDoc> listWithCountryName = this.countryRepo.findByCountriesCountry(name.toLowerCase());
        for (CountryDoc countryDoc : listWithCountryName) {
            countryDoc.setCountries(
                    countryDoc.getCountries().stream().filter(
                            country -> country.getCountry().equalsIgnoreCase(name))
                            .collect(Collectors.toList()));
        }
        return ResponseEntity.ok().body(listWithCountryName);
    }

    // TODO: add route to get total confirmed, deaths, recovered, active & average mortality and incident rate


    @DeleteMapping("delete_countries")
    public ResponseEntity<String> deleteAllCountries(@RequestBody(required = false) Map<String, String> password) {
        if (password == null || !password.containsKey("password")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Password required for delete route");
        }
        if (password.get("password").equals(environment.getProperty("delete.route.password"))) {
            this.countryRepo.deleteAll();
            return ResponseEntity.ok().body("Countries DB cleared");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password given for delete route");
        }
    }

}
