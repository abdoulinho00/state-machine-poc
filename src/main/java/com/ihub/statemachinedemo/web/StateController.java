package com.ihub.statemachinedemo.web;

import com.ihub.statemachinedemo.service.StateMachineHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/machines")
public class StateController {

    private final StateMachineHelper stateMachineHelper;

    @PostMapping("/{id}")
    public void createStateMachine(@PathVariable("id") String id, HttpServletRequest httpServletRequest) throws Exception {
        stateMachineHelper.createStateMachineFromSpecification(id, httpServletRequest.getInputStream());
    }

    @PostMapping("/{id}/start")
    public void startMachine(@PathVariable("id") String id) {
        stateMachineHelper.startStateMachine(id);
    }

    @PostMapping("/{id}/stop")
    public void stopMachine(@PathVariable("id") String id) {
        stateMachineHelper.stopStateMachine(id);
    }

    @GetMapping("/{id}")
    public String getStateMachine(@PathVariable("id") String id) {
        return stateMachineHelper.getCurrentState(id);
    }

    @PostMapping("/{id}/events/{event}")
    public void sendEvent(@PathVariable("id") String id, @PathVariable("event") String event) {
        stateMachineHelper.sendEvent(id, event);
    }
}
