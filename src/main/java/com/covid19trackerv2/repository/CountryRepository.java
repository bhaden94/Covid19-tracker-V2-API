package com.covid19trackerv2.repository;

import com.covid19trackerv2.model.country.CountryDoc;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CountryRepository extends MongoRepository<CountryDoc, String> {
    Optional<CountryDoc> findByDate(LocalDate date);

    List<CountryDoc> findByCountriesCountry(String name);

    Optional<CountryDoc> findTopByOrderByDateDesc();
}
