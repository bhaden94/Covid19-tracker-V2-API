package com.covid19trackerv2.controller;

import com.covid19trackerv2.model.state.StateDoc;
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

    @GetMapping("/all_states")
    public List<StateDoc> getStates() {
        return this.statesRepo.findAll();
    }



    @DeleteMapping("delete_states")
    public void deleteAllStates() {
        this.statesRepo.deleteAll();
    }

}
