package com.covid19trackerv2.repository;

import com.covid19trackerv2.model.state.StateDoc;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UsStateRepository extends MongoRepository<StateDoc, String> {
    Optional<StateDoc> findByDate(LocalDate date);

    List<StateDoc> findByStatesState(String name);
}
