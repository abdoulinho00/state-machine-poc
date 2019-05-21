package com.ihub.statemachinedemo.repository.mongodb;

import org.springframework.statemachine.data.mongodb.MongoDbStateMachineRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoStateRepository extends MongoDbStateMachineRepository {
}
