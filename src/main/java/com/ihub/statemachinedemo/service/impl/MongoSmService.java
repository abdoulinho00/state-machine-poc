package com.ihub.statemachinedemo.service.impl;

import com.ihub.statemachinedemo.repository.mongodb.SmStateRepository;
import com.ihub.statemachinedemo.repository.mongodb.SmTransitionRepository;
import com.ihub.statemachinedemo.service.SmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.scxml.model.SCXML;
import org.apache.commons.scxml.model.Transition;
import org.springframework.context.annotation.Profile;
import org.springframework.statemachine.data.mongodb.MongoDbRepositoryState;
import org.springframework.statemachine.data.mongodb.MongoDbRepositoryTransition;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@Profile("mongo")
@RequiredArgsConstructor
@Slf4j
public class MongoSmService implements SmService {

    private final SmStateRepository smStateRepository;
    private final SmTransitionRepository smTransitionRepository;

    @Override
    public void saveStates(SCXML scxml, String machineId) {
        Set<String> states = scxml.getTargets().keySet();
        String initial = getInitialState(scxml);
        states.stream().map(x-> getMongoDbRepositoryState(x, machineId, initial)).forEach(smStateRepository::save);
    }

    @Override
    public void saveTransitions(SCXML scxml, String machineId) {
        scxml.getTargets().values().forEach(x-> {
            org.apache.commons.scxml.model.State state = (org.apache.commons.scxml.model.State) x;

            state.getTransitionsList().forEach(y -> {
                Transition transition = (Transition) y;
                try {
                    String source = state.getId();
                    String target = transition.getNext(); //TODO to check further
                    String eventName = transition.getEvent();

                    MongoDbRepositoryTransition mongoDbRepositoryTransition = new MongoDbRepositoryTransition();
                    mongoDbRepositoryTransition.setMachineId(machineId);
                    mongoDbRepositoryTransition.setEvent(eventName);
                    mongoDbRepositoryTransition.setSource(smStateRepository.findFirstByMachineIdAndState(machineId, source).get());
                    mongoDbRepositoryTransition.setTarget(smStateRepository.findFirstByMachineIdAndState(machineId, target).get());

                    smTransitionRepository.save(mongoDbRepositoryTransition);
                } catch (Exception e) {
                    log.error("", e);
                }
            });
        });
    }


    private MongoDbRepositoryState getMongoDbRepositoryState(String state,String machineId, String initial){
        MongoDbRepositoryState mongoDbRepositoryState = new MongoDbRepositoryState();
        mongoDbRepositoryState.setMachineId(machineId);
        mongoDbRepositoryState.setState(state);
        mongoDbRepositoryState.setInitial(initial.equals(state));
        return  mongoDbRepositoryState;
    }

    private String getInitialState(SCXML scxml) {
        return scxml.getInitialTarget().getId();
    }
}
