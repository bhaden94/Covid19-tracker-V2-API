package com.covid19trackerv2.repository;

import com.covid19trackerv2.model.state.StateDoc;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UsStateRepository extends MongoRepository<StateDoc, String> {
}
