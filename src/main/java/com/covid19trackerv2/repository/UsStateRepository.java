package com.covid19trackerv2.repository;

import com.covid19trackerv2.model.charts.LineChart;
import com.covid19trackerv2.model.state.StateDoc;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UsStateRepository extends MongoRepository<StateDoc, String> {
    Optional<StateDoc> findByDate(LocalDate date);

    List<StateDoc> findByStatesState(String name);

    Optional<StateDoc> findTopByOrderByDateDesc();

    @Query
    Optional<LineChart[]> findAllDocuments();
}
