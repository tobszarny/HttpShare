package pl.biltech.httpshare.event.impl;

import pl.biltech.httpshare.event.Event;

public class DownloadWaitingForRequestEvent implements Event {

	private final String url;

	public DownloadWaitingForRequestEvent(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}
}
