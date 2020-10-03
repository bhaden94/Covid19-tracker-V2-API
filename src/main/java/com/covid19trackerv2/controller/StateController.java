package com.covid19trackerv2.controller;

import com.covid19trackerv2.model.state.StateDoc;
import com.covid19trackerv2.repository.UsStateRepository;
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
@RequestMapping("/api/state")
public class StateController {

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
    public void deleteAllStates() {
        this.statesRepo.deleteAll();
    }
}
