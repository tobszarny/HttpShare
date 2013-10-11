package pl.biltech.httpshare.event.impl;

import pl.biltech.httpshare.event.Event;

public class ErrorEvent implements Event {

	private final String errorMessage;

	public ErrorEvent(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
}
