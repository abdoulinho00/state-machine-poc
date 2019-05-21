package com.ihub.statemachinedemo.repository.redis;

import org.springframework.statemachine.data.redis.RedisStateRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedisSmStateRepository extends RedisStateRepository {
}
