package com.ihub.statemachinedemo.service.impl;

import com.ihub.statemachinedemo.repository.redis.RedisSmStateRepository;
import com.ihub.statemachinedemo.repository.redis.RedisSmTransitionRepository;
import com.ihub.statemachinedemo.service.SmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.scxml.model.SCXML;
import org.apache.commons.scxml.model.Transition;
import org.springframework.context.annotation.Profile;
import org.springframework.statemachine.data.redis.RedisRepositoryState;
import org.springframework.statemachine.data.redis.RedisRepositoryTransition;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@Profile("redis")
@RequiredArgsConstructor
@Slf4j
public class RedisSmService implements SmService {

    private final RedisSmStateRepository redisSmStateRepository;
    private final RedisSmTransitionRepository redisSmTransitionRepository;

    @Override
    public void saveStates(SCXML scxml, String machineId) {
        Set<String> states = scxml.getTargets().keySet();
        String initial = getInitialState(scxml);
        states.stream().map(x-> getMongoDbRepositoryState(x, machineId, initial)).forEach(redisSmStateRepository::save);
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

                    RedisRepositoryTransition redisRepositoryTransition = new RedisRepositoryTransition();
                    redisRepositoryTransition.setMachineId(machineId);
                    redisRepositoryTransition.setEvent(eventName);

                    redisRepositoryTransition.setSource(getRepositoryState(machineId, source));
                    redisRepositoryTransition.setTarget(getRepositoryState(machineId, target));

                    redisSmTransitionRepository.save(redisRepositoryTransition);
                } catch (Exception e) {
                    log.error("", e);
                }
            });
        });
    }

    private RedisRepositoryState getRepositoryState(String machineId, String state){
        return redisSmStateRepository.findByMachineId(machineId).stream().filter(x-> state.equals(x.getState())).findFirst().get();
    }

    private RedisRepositoryState getMongoDbRepositoryState(String state,String machineId, String initial){
        RedisRepositoryState redisRepositoryState = new RedisRepositoryState();
        redisRepositoryState.setMachineId(machineId);
        redisRepositoryState.setState(state);
        redisRepositoryState.setInitial(initial.equals(state));
        return  redisRepositoryState;
    }

    private String getInitialState(SCXML scxml) {
        return scxml.getInitialTarget().getId();
    }
}
