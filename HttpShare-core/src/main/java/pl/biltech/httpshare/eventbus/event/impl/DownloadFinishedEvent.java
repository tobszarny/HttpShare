package pl.biltech.httpshare.eventbus.event.impl;

import pl.biltech.httpshare.eventbus.event.Event;

/**
 * @author bilu
 * 
 */
public class DownloadFinishedEvent implements Event {

	private final String message;

	public DownloadFinishedEvent(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

}
