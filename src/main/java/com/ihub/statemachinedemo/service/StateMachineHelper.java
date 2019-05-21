package com.ihub.statemachinedemo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.scxml.io.SCXMLParser;
import org.apache.commons.scxml.model.SCXML;
import org.apache.commons.scxml.model.Transition;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.statemachine.service.StateMachineService;
import org.springframework.stereotype.Service;
import org.xml.sax.InputSource;

import java.io.InputStream;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class StateMachineHelper {

    private final StateMachineService<String,String> stateMachineService;
    private final SmService smService;

    public void createStateMachineFromSpecification(String id, InputStream inputStream) throws Exception {

        InputSource inputSource = new InputSource(inputStream);
        SCXML scxml = SCXMLParser.parse(inputSource, null);
        smService.saveStates(scxml, id);
        smService.saveTransitions(scxml, id);
    }

    private void configureStates(StateMachineBuilder.Builder<String, String> stateMachineBuilder, SCXML scxml) throws Exception {
        Set<String> states = scxml.getTargets().keySet();
        String intial = getInitialState(scxml);
        stateMachineBuilder.configureStates().withStates().states(states).initial(intial);
    }

    private String getInitialState(SCXML scxml) {
        return scxml.getInitialTarget().getId();
    }

    private void configureTransitions(StateMachineBuilder.Builder<String, String> stateMachineBuilder, SCXML scxml) throws Exception {
        scxml.getTargets().values().forEach(x-> configureTransition(stateMachineBuilder, x));
    }

    private void configureTransition(StateMachineBuilder.Builder<String, String> stateMachineBuilder, Object x) {
        org.apache.commons.scxml.model.State state = (org.apache.commons.scxml.model.State)x;

        state.getTransitionsList().forEach(y->{
            Transition transition = (Transition) y;
            try {
                String source= state.getId();
                String target = transition.getNext(); //TODO to check further
                String eventName = transition.getEvent();
                stateMachineBuilder.configureTransitions()
                        .withExternal()
                        .source(source)
                        .target(target)
                        .event(eventName);
            } catch (Exception e) {
                log.error("",e);
            }
        });
    }

    public String getCurrentState(String id) {
        return stateMachineService.acquireStateMachine(id).getState().getId();
    }

    public void startStateMachine(String id) {
        StateMachine<String, String> stateMachine = stateMachineService.acquireStateMachine(id);
        stateMachine.start();
    }

    public void stopStateMachine(String id) {
        StateMachine<String, String> stateMachine = stateMachineService.acquireStateMachine(id);
        stateMachine.stop();
    }

    public void sendEvent(String id, String event) {
        StateMachine<String, String> stateMachine = stateMachineService.acquireStateMachine(id);
        stateMachine.sendEvent(event);
    }
}
