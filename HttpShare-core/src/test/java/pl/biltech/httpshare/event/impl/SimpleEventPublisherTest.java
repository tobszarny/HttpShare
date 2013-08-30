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

	private Map<EventSubscriber<? extends Event>, Event> handledEvents;

	@Before
	public void before() {
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
		ep.publishSync(eventToPush);

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
		ep.publishSync(eventToPush);

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
		ep.publishSync(downloadStartedEvent);
		ep.publishSync(downloadProgressEvent);

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

	@Test
	// NOTE: This is mainly lerning test, it's rather about demonstrating how
	// async is working
	public void shouldAsyncPublishigBeSubscribersExceptionResistant() throws InterruptedException {
		EventPublisher ep = SimpleEventPublisher.INSTANCE;
		EventSubscriber<DownloadFinishedEvent> dse1 = createDownloadStaEventSubscriberWithExceptionThrown();
		EventSubscriber<DownloadFinishedEvent> dse2 = createDownloadStaEventSubscriberWithExceptionThrown();
		ep.addEventSubscriber(dse1);
		ep.addEventSubscriber(dse2);
		DownloadStartedEvent eventToPush = new DownloadStartedEvent("test message");

		ep.publishAsync(eventToPush);
		Thread.sleep(1000);
	}

	private EventSubscriber<DownloadFinishedEvent> createDownloadStaEventSubscriberWithExceptionThrown() {
		EventSubscriber<DownloadFinishedEvent> subscriber = new EventSubscriber<DownloadFinishedEvent>() {
			@Override
			public void handleEvent(DownloadFinishedEvent event) {
				throw new IllegalStateException("Intentionally thrown for: " + this);
			}
		};
		return subscriber;
	}

}
