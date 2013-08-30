package pl.biltech.httpshare.event.impl;

import static org.fest.assertions.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import pl.biltech.httpshare.event.Event;
import pl.biltech.httpshare.event.EventPublisher;
import pl.biltech.httpshare.event.EventSubscriber;

/**
 * @author bilu
 * 
 */
public class SimpleEventPublisherTest {

	// @Captor
	// private ArgumentCaptor<DownloadStartedEvent> downloadStartedEventCaptor;
	private Map<EventSubscriber<? extends Event>, Event> handledEvents;

	@Before
	public void before() {
		// initMocks(this);
		handledEvents = new HashMap<EventSubscriber<? extends Event>, Event>();
	}

	@Test
	public void shouldTheSameSubscribersReceiveTheSameEvent() throws Exception {
		// given
		EventPublisher ep = SimpleEventPublisher.INSTANCE;
		EventSubscriber<DownloadStartedEvent> dse1 = createDownloadStaEventSubscriber();
		EventSubscriber<DownloadStartedEvent> dse2 = createDownloadStaEventSubscriber();
		ep.addEventSubscriber(dse1);
		ep.addEventSubscriber(dse2);
		DownloadStartedEvent eventToPush = new DownloadStartedEvent("test message");

		// when
		ep.publish(eventToPush);

		// then
		assertThat(handledEvents).hasSize(2);
		assertThat(handledEvents.get(dse1)).isEqualTo(eventToPush);
		assertThat(handledEvents.get(dse2)).isEqualTo(eventToPush);
	}

	@Test
	public void shouldOnlyOneSubscriberReceiveTheEvent() throws Exception {
		// given
		EventPublisher ep = SimpleEventPublisher.INSTANCE;
		EventSubscriber<DownloadStartedEvent> dse1 = createDownloadStaEventSubscriber();
		EventSubscriber<DownloadProgressEvent> dse2 = createDownloadProgressEventSubscriber();
		ep.addEventSubscriber(dse1);
		ep.addEventSubscriber(dse2);
		DownloadStartedEvent eventToPush = new DownloadStartedEvent("test message");

		// when
		ep.publish(eventToPush);

		// then
		assertThat(handledEvents).hasSize(1);
		assertThat(handledEvents.get(dse1)).isEqualTo(eventToPush);
		assertThat(handledEvents.get(dse2)).isNull();
	}

	@Test
	public void shouldEachSubscriberReceiveCorrespondingEvent() throws Exception {
		// given
		EventPublisher ep = SimpleEventPublisher.INSTANCE;
		EventSubscriber<DownloadStartedEvent> dse1 = createDownloadStaEventSubscriber();
		EventSubscriber<DownloadProgressEvent> dse2 = createDownloadProgressEventSubscriber();
		ep.addEventSubscriber(dse1);
		ep.addEventSubscriber(dse2);
		DownloadStartedEvent downloadStartedEvent = new DownloadStartedEvent("test message");
		DownloadProgressEvent downloadProgressEvent = new DownloadProgressEvent(44);

		// when
		ep.publish(downloadStartedEvent);
		ep.publish(downloadProgressEvent);

		// then
		assertThat(handledEvents).hasSize(2);
		assertThat(handledEvents.get(dse1)).isEqualTo(downloadStartedEvent);
		assertThat(handledEvents.get(dse2)).isEqualTo(downloadProgressEvent);
	}

	private EventSubscriber<DownloadStartedEvent> createDownloadStaEventSubscriber() {
		EventSubscriber<DownloadStartedEvent> subscriber = new EventSubscriber<DownloadStartedEvent>() {
			@Override
			public void handleEvent(DownloadStartedEvent event) {
				handledEvents.put(this, event);
			}
		};
		return subscriber;
	}

	private EventSubscriber<DownloadProgressEvent> createDownloadProgressEventSubscriber() {
		EventSubscriber<DownloadProgressEvent> subscriber = new EventSubscriber<DownloadProgressEvent>() {
			@Override
			public void handleEvent(DownloadProgressEvent event) {
				handledEvents.put(this, event);
			}
		};
		return subscriber;
	}
}
