package com.covid19trackerv2.controller;

import com.covid19trackerv2.model.barChart.BarChart;
import com.covid19trackerv2.model.country.Country;
import com.covid19trackerv2.model.country.CountryDoc;
import com.covid19trackerv2.model.state.StateDoc;
import com.covid19trackerv2.model.state.UsState;
import com.covid19trackerv2.repository.CountryRepository;
import com.covid19trackerv2.repository.UsStateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class BarChartController {

    @Autowired
    private UsStateRepository statesRepo;

    @Autowired
    private CountryRepository countryRepo;

    @GetMapping("/state/bar_chart")
    public ResponseEntity<BarChart[]> getStateBarChart(@RequestParam String name) {
        // incident_rate at index 0 and mortality_rate at index 1
        BarChart[] chart = new BarChart[2];
        BarChart incident = new BarChart("incident_rate");
        BarChart mortality = new BarChart("mortality_rate");
        Optional<StateDoc> mostRecent = this.statesRepo.findTopByOrderByDateDesc();

        if(mostRecent.isPresent()) {
            // get rates for individual state
            for (UsState state : mostRecent.get().getStates()) {
                if(state.getState().equalsIgnoreCase(name)) {
                    incident.setStateCountry(state.getIncidentRate());
                    mortality.setStateCountry(state.getMortalityRate());
                }
            }
            // calculate all states average
            double incidentSum = 0.0;
            double mortalitySum = 0.0;
            for (UsState state : mostRecent.get().getStates()) {
               incidentSum += state.getIncidentRate();
               mortalitySum += state.getMortalityRate();
            }
            incident.setUsWorld(incidentSum / mostRecent.get().getStates().size());
            mortality.setUsWorld(mortalitySum / mostRecent.get().getStates().size());
        }

        chart[0] = incident;
        chart[1] = mortality;
        return ResponseEntity.ok().body(chart);
    }

    @GetMapping("/country/bar_chart")
    public ResponseEntity<BarChart[]> getCountryBarChart(@RequestParam String name) {
        // incident_rate at index 0 and mortality_rate at index 1
        BarChart[] chart = new BarChart[2];
        BarChart incident = new BarChart("incident_rate");
        BarChart mortality = new BarChart("mortality_rate");
        Optional<CountryDoc> mostRecent = this.countryRepo.findTopByOrderByDateDesc();

        if(mostRecent.isPresent()) {
            // get rates for individual country
            for(Country country : mostRecent.get().getCountries()) {
                if(country.getCountry().equalsIgnoreCase(name)) {
                    incident.setStateCountry(country.getIncidentRate());
                    mortality.setStateCountry(country.getMortalityRate());
                }
            }
            // calculate world averages
            double incidentSum = 0.0;
            double mortalitySum = 0.0;
            for(Country country : mostRecent.get().getCountries()) {
                incidentSum += country.getIncidentRate();
                mortalitySum += country.getMortalityRate();
            }
            incident.setUsWorld(incidentSum / mostRecent.get().getCountries().size());
            mortality.setUsWorld(mortalitySum / mostRecent.get().getCountries().size());
        }

        chart[0] = incident;
        chart[1] = mortality;
        return ResponseEntity.ok().body(chart);
    }
}
