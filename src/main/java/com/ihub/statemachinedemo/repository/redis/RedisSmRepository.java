package com.ihub.statemachinedemo.repository.redis;

import org.springframework.statemachine.data.redis.RedisStateMachineRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedisSmRepository extends RedisStateMachineRepository {

}
