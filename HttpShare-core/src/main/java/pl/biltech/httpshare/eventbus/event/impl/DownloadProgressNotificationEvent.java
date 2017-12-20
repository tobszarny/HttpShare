package pl.biltech.httpshare.eventbus.event.impl;

import pl.biltech.httpshare.eventbus.event.Event;
import pl.biltech.httpshare.util.Assert;

/**
 * Progress notify event with integers values for range <0, 100>
 * 
 * @author bilu
 * 
 */
public class DownloadProgressNotificationEvent implements Event {

	int percent;

	public DownloadProgressNotificationEvent(int percent) {
		Assert.assertTrue(percent >= 0, "Download percent can't be less than 0");
		Assert.assertTrue(percent <= 100, "Download percent can't be more than 100");
		this.percent = percent;
	}

	public int getPercent() {
		return percent;
	}
}
