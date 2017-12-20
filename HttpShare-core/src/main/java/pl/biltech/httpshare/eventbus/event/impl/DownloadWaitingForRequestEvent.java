package pl.biltech.httpshare.eventbus.event.impl;

import pl.biltech.httpshare.eventbus.event.Event;

public class DownloadWaitingForRequestEvent implements Event {

	private final String url;

	public DownloadWaitingForRequestEvent(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}
}
