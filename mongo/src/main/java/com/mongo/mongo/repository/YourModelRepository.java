package com.mongo.mongo.repository;

import com.mongo.mongo.model.YourModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface YourModelRepository extends MongoRepository<YourModel, String> {
}
