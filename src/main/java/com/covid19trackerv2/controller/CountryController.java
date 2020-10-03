package com.covid19trackerv2.controller;

import com.covid19trackerv2.model.country.CountryDoc;
import com.covid19trackerv2.model.state.StateDoc;
import com.covid19trackerv2.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/country")
public class CountryController {

    @Autowired
    private CountryRepository countryRepo;

    @GetMapping("/all")
    public ResponseEntity<List<CountryDoc>> getCountries() {
        return ResponseEntity.ok().body(this.countryRepo.findAll());
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<CountryDoc>> getCountriesPageable(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok().body(this.countryRepo.findAll(PageRequest.of(page, size)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getCountryById(@PathVariable String id) {
        Optional<CountryDoc> country = this.countryRepo.findById(id);
        return country.<ResponseEntity<Object>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().body("Country document not found with id " + id));
    }

    @GetMapping("/date")
    public ResponseEntity<Object> getStateByDate(
            @RequestParam int year,
            @RequestParam int month,
            @RequestParam int day
    ) {
        LocalDate date = LocalDate.of(year, month, day);
        Optional<CountryDoc> state = this.countryRepo.findByDate(date);
        return state.<ResponseEntity<Object>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest()
                        .body("Country document not found with date " + date.toString()));
    }



    @DeleteMapping("delete_countries")
    public void deleteAllCountries() {
        this.countryRepo.deleteAll();
    }
}
