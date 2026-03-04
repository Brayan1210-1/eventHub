package com.cesde.eventhub.exception.custom;

@SuppressWarnings("serial")
public class DataNotFound extends RuntimeException {

	public DataNotFound(String message) {
		super(message);
	}
}
