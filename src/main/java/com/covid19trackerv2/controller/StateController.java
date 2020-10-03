package com.covid19trackerv2.controller;

import com.covid19trackerv2.model.state.StateDoc;
import com.covid19trackerv2.repository.UsStateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class StateController {

    @Autowired
    private UsStateRepository statesRepo;

    @GetMapping("/all_states")
    public List<StateDoc> getStates() {
        return this.statesRepo.findAll();
    }

    @GetMapping("/states")
    public Page<StateDoc> getStatesPageable(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return this.statesRepo.findAll(PageRequest.of(page, size));
    }

    @DeleteMapping("delete_states")
    public void deleteAllStates() {
        this.statesRepo.deleteAll();
    }
}
