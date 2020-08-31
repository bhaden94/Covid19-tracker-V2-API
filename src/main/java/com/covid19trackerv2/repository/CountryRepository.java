package com.covid19trackerv2.repository;

import com.covid19trackerv2.model.country.CountryDoc;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CountryRepository extends MongoRepository<CountryDoc, String> {
}
