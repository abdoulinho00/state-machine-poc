package com.ihub.statemachinedemo.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StateMachineServiceTest {

    @Autowired
    private StateMachineHelper stateMachineHelper;

    @Test
    public void testInitialState() throws Exception {

        stateMachineHelper.startStateMachine(null);

    }

}
