package com.covid19trackerv2.repository;

import com.covid19trackerv2.model.state.StateDoc;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;

public interface UsStateRepository extends MongoRepository<StateDoc, String> {
    StateDoc findByDate(LocalDate date);
}
