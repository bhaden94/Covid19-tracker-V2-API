package com.covid19trackerv2.controller;

import com.covid19trackerv2.model.state.StateDoc;
import com.covid19trackerv2.repository.UsStateRepository;
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
    public ResponseEntity<List<StateDoc>> getCountryByName(@RequestParam String name) {
        List<StateDoc> listWithStateName = this.statesRepo.findByStatesState(name.toLowerCase());
        for (StateDoc stateDoc : listWithStateName) {
            stateDoc.setStates(
                    stateDoc.getStates().stream().filter(
                            country -> country.getState().equalsIgnoreCase(name))
                            .collect(Collectors.toList()));
        }
        return ResponseEntity.ok().body(listWithStateName);
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
