package com.cesde.eventhub.exception.custom;

@SuppressWarnings("serial")
public class NotMatch extends RuntimeException{

	public NotMatch(String message) {
		super(message);
	}
}
