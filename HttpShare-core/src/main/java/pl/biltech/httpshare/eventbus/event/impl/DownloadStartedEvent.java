package pl.biltech.httpshare.eventbus.event.impl;

import pl.biltech.httpshare.eventbus.event.Event;

/**
 * @author bilu
 * 
 */
public class DownloadStartedEvent implements Event {

	private final String message;

	public DownloadStartedEvent(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

}
