package com.cesde.eventhub.exception.custom;

@SuppressWarnings("serial")
public class InvalidRegistration extends RuntimeException {

	public InvalidRegistration(String message) {
        super(message);
    }
}
