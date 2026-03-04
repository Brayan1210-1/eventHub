package com.cesde.eventhub.exception.custom;

@SuppressWarnings("serial")
public class InvalidUserRegistration extends RuntimeException {

	public InvalidUserRegistration(String message) {
        super(message);
    }
}
