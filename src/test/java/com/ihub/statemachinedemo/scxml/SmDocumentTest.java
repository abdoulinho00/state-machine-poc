package com.ihub.statemachinedemo.scxml;

import com.ihub.statemachinedemo.service.StateMachineHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.scxml.io.SCXMLParser;
import org.apache.commons.scxml.model.ModelException;
import org.apache.commons.scxml.model.SCXML;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@Slf4j
public class SmDocumentTest {

    @Test
    public void readDocument() throws ModelException, SAXException, IOException {

        InputSource inputSource = new InputSource(new ClassPathResource("hello.xml").getInputStream());
        SCXML scxml = SCXMLParser.parse(inputSource, null);

        Map map = scxml.getTargets();
        log.info("{}", map);

    }

    @Test
    public void createStateMachineTest() throws Exception {
        InputStream inputStream = new ClassPathResource("hello.xml").getInputStream();
        String id = "1";
        StateMachineHelper stateMachineHelper = new StateMachineHelper(null,null);

        stateMachineHelper.createStateMachineFromSpecification(id, inputStream);
        stateMachineHelper.startStateMachine(id);

        assertEquals("hello", stateMachineHelper.getCurrentState(id));

        stateMachineHelper.sendEvent(id, "init");

        assertEquals("off", stateMachineHelper.getCurrentState(id));

        stateMachineHelper.sendEvent(id, "turn.on");

        assertEquals("on", stateMachineHelper.getCurrentState(id));

    }

}
