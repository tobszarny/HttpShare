package pl.biltech.httpshare.eventbus.subscriber.impl;

import org.junit.Assert;
import org.junit.Test;
import pl.biltech.httpshare.eventbus.event.impl.DownloadFinishedEvent;

import static org.hamcrest.core.Is.is;

public class DownloadFinishedEventSubscriberTest {

    @Test
    public void isEventAMatch() {
        DownloadFinishedEventSubscriber instance = new DownloadFinishedEventSubscriber(null);

        Assert.assertThat(instance.isEventAMatch(new DownloadFinishedEvent("")), is(true));
    }
}