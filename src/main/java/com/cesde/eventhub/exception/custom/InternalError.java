package com.cesde.eventhub.exception.custom;

@SuppressWarnings("serial")
public class InternalError extends RuntimeException {
	public InternalError(String message) {
        super(message);
    }

}
