package com.covid19trackerv2.repository;

import com.covid19trackerv2.model.charts.LineChart;
import com.covid19trackerv2.model.country.CountryDoc;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CountryRepository extends MongoRepository<CountryDoc, String> {
    Optional<CountryDoc> findByDate(LocalDate date);

    List<CountryDoc> findByCountriesCountry(String name);

    Optional<CountryDoc> findTopByOrderByDateDesc();

    // performs sum of all fields in sub-document states
    @Aggregation(pipeline = {"" +
            "{'$project': {'_id': '$_id', 'date': '$date', \n" +
            "      'confirmed': {'$sum': '$countries.confirmed'}, \n" +
            "      'deaths': {'$sum': '$countries.deaths'}, \n" +
            "      'recovered': {'$sum': '$countries.recovered'}, \n" +
            "      'active': {'$sum': '$countries.active'}}}" +
            "}"
    })
    AggregationResults<LineChart> aggregateAllCountries(Sort sort);

    // filters sub-document of states to be the state we are searching for
    // then performs sum of all fields in sub-document states
    @Aggregation(pipeline = {"" +
            "{'$project': {'_id': '$_id', 'date': '$date', \n" +
            "      'countries': {'$filter': {\n" +
            "          'input': '$countries', \n" +
            "          'as': 'item', \n" +
            "          'cond': {'$eq': ['$$item.country', '?0']}}}}" +
            "}",
            "{'$project': {'_id': '$_id', 'date': '$date', \n" +
            "      'confirmed': {'$sum': '$countries.confirmed'}, \n" +
            "      'deaths': {'$sum': '$countries.deaths'}, \n" +
            "      'recovered': {'$sum': '$countries.recovered'}, \n" +
            "      'active': {'$sum': '$countries.active'}}}" +
            "}"
    })
    AggregationResults<LineChart> aggregateOneCountry(String name, Sort sort);
}
