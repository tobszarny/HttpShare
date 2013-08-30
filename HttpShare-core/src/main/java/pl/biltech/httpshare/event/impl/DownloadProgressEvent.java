package pl.biltech.httpshare.event.impl;

import pl.biltech.httpshare.event.Event;
import pl.biltech.httpshare.util.Assert;

/**
 * Progress notify event with integers values for range <0, 100>
 * 
 * @author bilu
 * 
 */
public class DownloadProgressEvent implements Event {

	int percent;

	public DownloadProgressEvent(int percent) {
		Assert.isTrue(percent >= 0, "Download percent can't be less than 0");
		Assert.isTrue(percent <= 100, "Download percent can't be more than 100");
		this.percent = percent;
	}

	public int getPercent() {
		return percent;
	}
}
