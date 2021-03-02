package com.covid19trackerv2.repository;

import com.covid19trackerv2.model.charts.LineChart;
import com.covid19trackerv2.model.state.StateDoc;
import com.fasterxml.jackson.databind.util.JSONPObject;
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

    @Aggregation(pipeline = {"{\n" +
            "    '$project': {\n" +
            "      '_id': '$_id', \n" +
            "      'date': '$date', \n" +
            "      'confirmed': {\n" +
            "        '$sum': '$states.confirmed'\n" +
            "      }, \n" +
            "      'deaths': {\n" +
            "        '$sum': '$states.deaths'\n" +
            "      }, \n" +
            "      'recovered': {\n" +
            "        '$sum': '$states.recovered'\n" +
            "      }, \n" +
            "      'active': {\n" +
            "        '$sum': '$states.active'\n" +
            "      }\n" +
            "    }\n" +
            "  }, {\n" +
            "    '$sort': {\n" +
            "      'date': 1\n" +
            "    }\n" +
            "  }"
    })
    AggregationResults<LineChart> aggregateAllStates();
}
