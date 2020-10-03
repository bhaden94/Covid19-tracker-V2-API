package com.covid19trackerv2.controller;

import com.covid19trackerv2.model.state.StateDoc;
import com.covid19trackerv2.repository.UsStateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/state")
public class StateController {

    @Autowired
    Environment environment;

    @Autowired
    private UsStateRepository statesRepo;

    @GetMapping("/all")
    public ResponseEntity<List<StateDoc>> getStates() {
        return ResponseEntity.ok().body(this.statesRepo.findAll());
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<StateDoc>> getStatesPageable(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok().body(this.statesRepo.findAll(PageRequest.of(page, size)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getStateById(@PathVariable String id) {
        Optional<StateDoc> state = this.statesRepo.findById(id);
        return state.<ResponseEntity<Object>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().body("State document not found with id " + id));
    }

    @GetMapping("/date")
    public ResponseEntity<Object> getStateByDate(
            @RequestParam int year,
            @RequestParam int month,
            @RequestParam int day
    ) {
        LocalDate date = LocalDate.of(year, month, day);
        Optional<StateDoc> state = this.statesRepo.findByDate(date);
        return state.<ResponseEntity<Object>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest()
                        .body("State document not found with date " + date.toString()));
    }



    @DeleteMapping("delete_states")
    public ResponseEntity<String> deleteAllStates(@RequestBody(required = false) Map<String, String> password) {
        if(password == null || !password.containsKey("password")) {
            return ResponseEntity.badRequest().body("Password required for delete route");
        }
        if(password.get("password").equals(environment.getProperty("DB_PASSWORD"))) {
            this.statesRepo.deleteAll();
            return ResponseEntity.ok().body("States DB cleared");
        } else {
            return ResponseEntity.badRequest().body("Invalid password given for delete route");
        }
    }
}
