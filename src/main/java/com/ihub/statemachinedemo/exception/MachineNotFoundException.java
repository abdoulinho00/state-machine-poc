package com.ihub.statemachinedemo.exception;

public class MachineNotFoundException extends Exception{

    private String message;

    public MachineNotFoundException(){
        this.message = "machine not found";
    }
}
