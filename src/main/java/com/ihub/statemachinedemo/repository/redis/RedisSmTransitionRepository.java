package com.ihub.statemachinedemo.repository.redis;

import org.springframework.statemachine.data.redis.RedisTransitionRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedisSmTransitionRepository extends RedisTransitionRepository {
}
