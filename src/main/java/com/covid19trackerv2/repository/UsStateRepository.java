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

    @Aggregation(pipeline = {"{'$project': {'_id': '$_id', 'date': '$date', \n" +
            "      'confirmed': {'$sum': '$states.confirmed'}, \n" +
            "      'deaths': {'$sum': '$states.deaths'}, \n" +
            "      'recovered': {'$sum': '$states.recovered'}, \n" +
            "      'active': {'$sum': '$states.active'}}}}"
    })
    AggregationResults<LineChart> aggregateAllStates(Sort sort);

    @Aggregation(pipeline = {"{'$unwind':{path:'$states'}},\n" +
            "  {'$match': {'states.state': 'alabama'}},\n" +
            "  {'$project':{_id:'$_id', date: '$date',\n" +
            "    confirmed: {'$sum': 'confirmed'},\n" +
            "    active: {'$sum': 'active'},\n" +
            "    deaths: {'$sum': 'deaths'},\n" +
            "    recovered: {'$sum': 'recovered'}\n" +
            "  }}"
    })
    AggregationResults<LineChart> aggregateOneState(String name, Sort sort);
}
