package com.covid19trackerv2.controller;

import com.covid19trackerv2.model.country.CountryDoc;
import com.covid19trackerv2.model.state.StateDoc;
import com.covid19trackerv2.repository.CountryRepository;
import com.covid19trackerv2.repository.UsStateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class WebController {

    @Autowired
    private UsStateRepository statesRepo;

    @Autowired
    private CountryRepository countryRepo;

    @GetMapping("/all_states")
    public List<StateDoc> getStates() {
        return this.statesRepo.findAll();
    }

    @GetMapping("/all_countries")
    public List<CountryDoc> getCountries() { return this.countryRepo.findAll(); }



    @DeleteMapping("delete_states")
    public void deleteAllStates() {
        this.statesRepo.deleteAll();
    }

    @DeleteMapping("delete_countries")
    public void deleteAllCountries() {
        this.countryRepo.deleteAll();
    }

}
