package com.ihub.statemachinedemo.repository.mongodb;

import org.springframework.statemachine.data.mongodb.MongoDbTransitionRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SmTransitionRepository extends MongoDbTransitionRepository {
}
