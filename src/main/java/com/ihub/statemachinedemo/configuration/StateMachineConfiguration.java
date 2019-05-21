package com.ihub.statemachinedemo.configuration;

import com.ihub.statemachinedemo.repository.mongodb.SmStateRepository;
import com.ihub.statemachinedemo.repository.mongodb.SmTransitionRepository;
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
import org.springframework.statemachine.data.mongodb.MongoDbPersistingStateMachineInterceptor;
import org.springframework.statemachine.data.mongodb.MongoDbStateMachineRepository;
import org.springframework.statemachine.monitor.StateMachineMonitor;
import org.springframework.statemachine.security.SecurityRule;
import org.springframework.statemachine.service.DefaultStateMachineService;
import org.springframework.statemachine.service.StateMachineService;

import java.util.ArrayList;
import java.util.Collections;

@Configuration
@Profile("mongo")
public class StateMachineConfiguration {

    @Bean
    StateMachineService<String,String> getStateMachineService(StateMachineFactory<String,String> stateMachineFactory,MongoDbPersistingStateMachineInterceptor mongoDbPersistingStateMachineInterceptor ){
        return new DefaultStateMachineService<String,String>(stateMachineFactory, mongoDbPersistingStateMachineInterceptor);
    }

    @Bean
    MongoDbPersistingStateMachineInterceptor getInterceptor(MongoDbStateMachineRepository mongoDbStateMachineRepository){
        return new MongoDbPersistingStateMachineInterceptor(mongoDbStateMachineRepository);
    }

    @Bean
    StateMachineFactory<String,String> getStateMachineFactory(ConfigurationData<String,String> configurationData, RepositoryStateMachineModelFactory repositoryStateMachineModelFactory){
        return new ObjectStateMachineFactory(new DefaultStateMachineModel(configurationData, null, null), repositoryStateMachineModelFactory);
    }

    @Bean
    ConfigurationData<String,String> getConfigurationData(MongoDbPersistingStateMachineInterceptor mongoDbPersistingStateMachineInterceptor){
        return new ConfigurationData<String,String>(null, new SyncTaskExecutor(), new ConcurrentTaskScheduler(), false, null, new ArrayList(), false, (AccessDecisionManager)null, (AccessDecisionManager)null, (SecurityRule)null, (SecurityRule)null, true, new DefaultStateMachineModelVerifier(), (String)null, (StateMachineMonitor)null, Collections
                .singletonList(mongoDbPersistingStateMachineInterceptor));
    }

    @Bean
    RepositoryStateMachineModelFactory getRepositoryStateMachineModelFactory(SmStateRepository smStateRepository, SmTransitionRepository smTransitionRepository){
        return new RepositoryStateMachineModelFactory(smStateRepository, smTransitionRepository);
    }
}
