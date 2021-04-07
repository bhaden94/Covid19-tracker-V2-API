package com.covid19trackerv2.controller;

import com.covid19trackerv2.model.state.StateDoc;
import com.covid19trackerv2.model.state.UsState;
import com.covid19trackerv2.repository.UsStateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/api/state")
public class StateController {

    @Autowired
    private Environment environment;

    @Autowired
    private UsStateRepository statesRepo;

    @GetMapping("/all")
    public ResponseEntity<List<StateDoc>> getStates() {
        return ResponseEntity.ok().body(this.statesRepo.findAll());
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<StateDoc>> getStatesPageable(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok().body(this.statesRepo.findAll(PageRequest.of(page, size)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getStateById(@PathVariable String id) {
        Optional<StateDoc> state = this.statesRepo.findById(id);
        return state.<ResponseEntity<Object>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.ok().body("State document not found with id " + id));
    }

    @GetMapping("/date")
    public ResponseEntity<Object> getStateByDate(
            @RequestParam int year,
            @RequestParam int month,
            @RequestParam int day) {
        LocalDate date = LocalDate.of(year, month, day);
        Optional<StateDoc> state = this.statesRepo.findByDate(date);
        return state.<ResponseEntity<Object>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.ok()
                        .body("State document not found with date " + date.toString()));
    }

    @GetMapping("")
    public ResponseEntity<List<StateDoc>> getStateByName(@RequestParam String name) {
        // find all docs that have name of state in it
        List<StateDoc> listWithStateName = this.statesRepo.findByStatesState(name.toLowerCase(Locale.US));
        // filter the list to only include that state
        for (StateDoc stateDoc : listWithStateName) {
            stateDoc.setStates(
                    stateDoc.getStates().stream().filter(
                            state -> state.getState().equalsIgnoreCase(name))
                            .collect(Collectors.toList()));
        }
        return ResponseEntity.ok().body(listWithStateName);
    }

    @GetMapping("/totals")
    public ResponseEntity<Map<String, Long>> getStateTotals(@RequestParam(required = false) String name) {
        Map<String, Long> totals = new HashMap<>();
        totals.put("confirmed", 0L);
        totals.put("active", 0L);
        totals.put("recovered", 0L);
        totals.put("deaths", 0L);
        // gets single document by most recent date
        Optional<StateDoc> mostRecent = this.statesRepo.findTopByOrderByDateDesc();
        if (mostRecent.isPresent()) {
            for (UsState state : mostRecent.get().getStates()) {
                // if we only care about one state then stop there
                if (name == null) {
                    totals.put("confirmed", totals.get("confirmed") + state.getConfirmed());
                    totals.put("active", totals.get("active") + state.getActive());
                    totals.put("recovered", totals.get("recovered") + state.getRecovered());
                    totals.put("deaths", totals.get("deaths") + state.getDeaths());
                } else {
                    if (state.getState().equalsIgnoreCase(name)) {
                        totals.put("confirmed", state.getConfirmed());
                        totals.put("active", state.getActive());
                        totals.put("recovered", state.getRecovered());
                        totals.put("deaths", state.getDeaths());
                        return ResponseEntity.ok().body(totals);
                    }
                }
            }
        }
        return ResponseEntity.ok().body(totals);
    }

    @GetMapping("/rates/incident_rate")
    public ResponseEntity<Map<String, Double>> getStateIncidentRate(@RequestParam(required = false) String name) {
        Map<String, Double> rate = new HashMap<>();
        rate.put("incident_rate", 0.0);
        // gets single document by most recent date
        Optional<StateDoc> mostRecent = this.statesRepo.findTopByOrderByDateDesc();
        double sum = 0.0;
        if (mostRecent.isPresent()) {
            for (UsState state : mostRecent.get().getStates()) {
                // if we are only looking for one state then we can return its rate immediately
                if (name == null) {
                    sum += state.getIncidentRate();
                } else {
                    if (state.getState().equalsIgnoreCase(name)) {
                        rate.put("incident_rate", state.getIncidentRate());
                        return ResponseEntity.ok().body(rate);
                    }
                }
            }
            rate.put("incident_rate", sum / mostRecent.get().getStates().size());
        }
        return ResponseEntity.ok().body(rate);
    }

    @GetMapping("/rates/mortality_rate")
    public ResponseEntity<Map<String, Double>> getStateMortalityRate(@RequestParam(required = false) String name) {
        Map<String, Double> rate = new HashMap<>();
        rate.put("mortality_rate", 0.0);
        // gets single document by most recent date
        Optional<StateDoc> mostRecent = this.statesRepo.findTopByOrderByDateDesc();
        double sum = 0.0;
        if (mostRecent.isPresent()) {
            for (UsState state : mostRecent.get().getStates()) {
                // if we are only looking for one state then we can return its rate immediately
                if (name == null) {
                    sum += state.getMortalityRate();
                } else {
                    if (state.getState().equalsIgnoreCase(name)) {
                        rate.put("mortality_rate", state.getMortalityRate());
                        return ResponseEntity.ok().body(rate);
                    }
                }
            }
            rate.put("mortality_rate", sum / mostRecent.get().getStates().size());
        }
        return ResponseEntity.ok().body(rate);
    }

    @GetMapping("/days_difference")
    public ResponseEntity<Map<String, Long>> getDaysDifference(@RequestParam Integer diff) {
        // gets single document by most recent date
        Optional<StateDoc> mostRecent = this.statesRepo.findTopByOrderByDateDesc();
        if (mostRecent.isPresent()) {
            LocalDate prevDate = mostRecent.get().getDate().minusDays(diff);
            // get entry for {diff} number of days difference
            Optional<StateDoc> prevEntry = this.statesRepo.findByDate(prevDate);
            // we want the states array to be the same size so we know they correspond to the same states
            // at each index
            if (prevEntry.isPresent() && prevEntry.get().getStates().size() == mostRecent.get().getStates().size()) {
                Map<String, Long> diffMap = new LinkedHashMap<>();
                StateDoc curr = mostRecent.get();
                StateDoc prev = prevEntry.get();
                // build diffMap
                for (int i = 0; i < prev.getStates().size(); i++) {
                    // if the states are the same then we are good
                    // will need to put in some error handling here in case the states are not the same
                    if (prev.getStates().get(i).getState().equalsIgnoreCase(curr.getStates().get(i).getState())) {
                        long difference = curr.getStates().get(i).getConfirmed() - prev.getStates().get(i).getConfirmed();
                        diffMap.put(prev.getStates().get(i).getState(), difference);
                    }
                }
                return ResponseEntity.ok().body(diffMap);
            }
        }
        // not finding an entry should not happen since it is just looking for the most recent
        return ResponseEntity.notFound().build();
    }


    @DeleteMapping("delete_states")
    public ResponseEntity<String> deleteAllStates(@RequestBody(required = false) Map<String, String> password) {
        if (password == null || !password.containsKey("password")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Password required for delete route");
        }
        if (password.get("password").equals(environment.getProperty("delete.route.password"))) {
            this.statesRepo.deleteAll();
            return ResponseEntity.ok().body("States DB cleared");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password given for delete route");
        }
    }

}
