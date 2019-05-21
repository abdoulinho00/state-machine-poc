package com.ihub.statemachinedemo.repository.mongodb;

import org.springframework.statemachine.data.mongodb.MongoDbRepositoryState;
import org.springframework.statemachine.data.mongodb.MongoDbStateRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SmStateRepository extends MongoDbStateRepository {

    Optional<MongoDbRepositoryState> findFirstByMachineIdAndState(String machineId, String State);
}
