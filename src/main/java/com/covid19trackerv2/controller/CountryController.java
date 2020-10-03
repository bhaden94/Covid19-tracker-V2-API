package com.covid19trackerv2.controller;

import com.covid19trackerv2.model.country.CountryDoc;
import com.covid19trackerv2.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class CountryController {

    @Autowired
    private CountryRepository countryRepo;

    @GetMapping("/all_countries")
    public List<CountryDoc> getCountries() { return this.countryRepo.findAll(); }

    @GetMapping("/countries")
    public Page<CountryDoc> getCountriesPageable(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return this.countryRepo.findAll(PageRequest.of(page, size));
    }

    @DeleteMapping("delete_countries")
    public void deleteAllCountries() {
        this.countryRepo.deleteAll();
    }
}
