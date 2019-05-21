package com.ihub.statemachinedemo.service;

import org.apache.commons.scxml.model.SCXML;

public interface SmService {

    void saveStates(SCXML scxml, String machineId);
    void saveTransitions(SCXML scxml, String machineId);
}
