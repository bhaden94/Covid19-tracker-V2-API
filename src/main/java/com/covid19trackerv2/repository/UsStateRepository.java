package com.covid19trackerv2.repository;

import com.covid19trackerv2.model.charts.LineChart;
import com.covid19trackerv2.model.state.StateDoc;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UsStateRepository extends MongoRepository<StateDoc, String> {
    Optional<StateDoc> findByDate(LocalDate date);

    List<StateDoc> findByStatesState(String name);

    Optional<StateDoc> findTopByOrderByDateDesc();

    // performs sum of all fields in sub-document states
    @Aggregation(pipeline = {"" +
            "{'$project': {'_id': '$_id', 'date': '$date', \n" +
            "      'confirmed': {'$sum': '$states.confirmed'}, \n" +
            "      'deaths': {'$sum': '$states.deaths'}, \n" +
            "      'recovered': {'$sum': '$states.recovered'}, \n" +
            "      'active': {'$sum': '$states.active'}}}" +
            "}"
    })
    AggregationResults<LineChart> aggregateAllStates(Sort sort);

    // filters sub-document of states to be the state we are searching for
    // then performs sum of all fields in sub-document states
    @Aggregation(pipeline = {"" +
            "{'$project': {'_id': '$_id', 'date': '$date', \n" +
            "      'states': {'$filter': {\n" +
            "          'input': '$states', \n" +
            "          'as': 'item', \n" +
            "          'cond': {'$eq': ['$$item.state', '?0']}}}}" +
            "}",
            "{'$project': {'_id': '$_id', 'date': '$date', \n" +
            "      'confirmed': {'$sum': '$states.confirmed'}, \n" +
            "      'deaths': {'$sum': '$states.deaths'}, \n" +
            "      'recovered': {'$sum': '$states.recovered'}, \n" +
            "      'active': {'$sum': '$states.active'}}}" +
            "}"
    })
    AggregationResults<LineChart> aggregateOneState(String name, Sort sort);
}
