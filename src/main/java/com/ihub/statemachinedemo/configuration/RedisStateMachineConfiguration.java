package com.ihub.statemachinedemo.configuration;

import com.ihub.statemachinedemo.repository.redis.RedisSmStateRepository;
import com.ihub.statemachinedemo.repository.redis.RedisSmTransitionRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.statemachine.config.ObjectStateMachineFactory;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.config.model.ConfigurationData;
import org.springframework.statemachine.config.model.DefaultStateMachineModel;
import org.springframework.statemachine.config.model.verifier.DefaultStateMachineModelVerifier;
import org.springframework.statemachine.data.RepositoryStateMachineModelFactory;
import org.springframework.statemachine.data.redis.RedisPersistingStateMachineInterceptor;
import org.springframework.statemachine.data.redis.RedisStateMachineRepository;
import org.springframework.statemachine.monitor.StateMachineMonitor;
import org.springframework.statemachine.security.SecurityRule;
import org.springframework.statemachine.service.DefaultStateMachineService;
import org.springframework.statemachine.service.StateMachineService;

import java.util.ArrayList;
import java.util.Collections;

@Configuration
@Profile("redis")
public class RedisStateMachineConfiguration {

    @Bean
    public StateMachineService<String, String> getStateMachineService(StateMachineFactory<String,String> stateMachineFactory, RedisPersistingStateMachineInterceptor redisPersistingStateMachineInterceptor){
        return new DefaultStateMachineService<String,String>(stateMachineFactory, redisPersistingStateMachineInterceptor);
    }

    @Bean
    public RedisPersistingStateMachineInterceptor getRedisInterceptor(RedisStateMachineRepository redisStateMachineRepository){
        return new RedisPersistingStateMachineInterceptor(redisStateMachineRepository);
    }

    @Bean
    public StateMachineFactory<String, String> getStateMachineFactory(ConfigurationData<String,String> configurationData , RepositoryStateMachineModelFactory repositoryStateMachineModelFactory){
        return new ObjectStateMachineFactory(new DefaultStateMachineModel(configurationData, null, null), repositoryStateMachineModelFactory);
    }

    @Bean
    public ConfigurationData<String,String> getConfigurationData(RedisPersistingStateMachineInterceptor redisPersistingStateMachineInterceptor){
        return new ConfigurationData<String,String>(null, new SyncTaskExecutor(), new ConcurrentTaskScheduler(), false, null, new ArrayList(), false, (AccessDecisionManager)null, (AccessDecisionManager)null, (SecurityRule)null, (SecurityRule)null, true, new DefaultStateMachineModelVerifier(), (String)null, (StateMachineMonitor)null, Collections
                .singletonList(redisPersistingStateMachineInterceptor));
    }

    @Bean
    public RepositoryStateMachineModelFactory getRepositoryStateMachineModelFactory(RedisSmStateRepository redisSmStateRepository , RedisSmTransitionRepository redisSmTransitionRepository){
        return new RepositoryStateMachineModelFactory(redisSmStateRepository, redisSmTransitionRepository);
    }

}
