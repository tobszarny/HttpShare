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
 * NOTE: This are mainly lerning tests, it's rather about demonstrating how
 * async should work. Consider @Ignor addition
 * 
 * @author bilu
 * 
 */
public class AsyncEventManagerTest {

	private static final int MILLIS = 200;
	private Map<EventSubscriber<? extends Event>, Event> handledEvents;
	private AsyncEventManager eventManager;
	private EventPublisher eventPublisher;

	@Before
	public void before() {
		handledEvents = new HashMap<EventSubscriber<? extends Event>, Event>();
		eventManager = AsyncEventManager.INSTANCE;
		eventPublisher = eventManager.createEventPublisher();

	}

	@Test
	public void shouldTheSameSubscribersReceiveTheSameEvent() throws Exception {
		// given
		EventSubscriber<DownloadStartedEvent> dse1 = createDownloadStaEventSubscriber();
		EventSubscriber<DownloadStartedEvent> dse2 = createDownloadStaEventSubscriber();
		eventManager.addEventSubscriber(dse1);
		eventManager.addEventSubscriber(dse2);
		DownloadStartedEvent eventToPush = new DownloadStartedEvent("test message");

		// when
		eventPublisher.publish(eventToPush);

		// then
		Thread.sleep(MILLIS);
		assertThat(handledEvents).hasSize(2);
		assertThat(handledEvents.get(dse1)).isEqualTo(eventToPush);
		assertThat(handledEvents.get(dse2)).isEqualTo(eventToPush);
	}

	@Test
	public void shouldOnlyOneSubscriberReceiveTheEvent() throws Exception {
		// given
		EventSubscriber<DownloadStartedEvent> dse1 = createDownloadStaEventSubscriber();
		EventSubscriber<DownloadProgressNotificationEvent> dse2 = createDownloadProgressEventSubscriber();
		eventManager.addEventSubscriber(dse1);
		eventManager.addEventSubscriber(dse2);
		DownloadStartedEvent eventToPush = new DownloadStartedEvent("test message");

		// when
		eventPublisher.publish(eventToPush);

		// then
		Thread.sleep(MILLIS);
		assertThat(handledEvents).hasSize(1);
		assertThat(handledEvents.get(dse1)).isEqualTo(eventToPush);
		assertThat(handledEvents.get(dse2)).isNull();
	}

	@Test
	public void shouldEachSubscriberReceiveCorrespondingEvent() throws Exception {
		// given
		EventSubscriber<DownloadStartedEvent> dse1 = createDownloadStaEventSubscriber();
		EventSubscriber<DownloadProgressNotificationEvent> dse2 = createDownloadProgressEventSubscriber();
		eventManager.addEventSubscriber(dse1);
		eventManager.addEventSubscriber(dse2);
		DownloadStartedEvent downloadStartedEvent = new DownloadStartedEvent("test message");
		DownloadProgressNotificationEvent downloadProgressEvent = new DownloadProgressNotificationEvent(44);

		// when
		eventPublisher.publish(downloadStartedEvent);
		eventPublisher.publish(downloadProgressEvent);

		// then
		Thread.sleep(MILLIS);
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

	private EventSubscriber<DownloadProgressNotificationEvent> createDownloadProgressEventSubscriber() {
		EventSubscriber<DownloadProgressNotificationEvent> subscriber = new EventSubscriber<DownloadProgressNotificationEvent>() {
			@Override
			public void handleEvent(DownloadProgressNotificationEvent event) {
				handledEvents.put(this, event);
			}
		};
		return subscriber;
	}

	@Test
	public void shouldAsyncPublishigBeSubscribersExceptionResistant() throws InterruptedException {
		EventSubscriber<DownloadFinishedEvent> dse1 = createDownloadStaEventSubscriberWithExceptionThrown();
		EventSubscriber<DownloadFinishedEvent> dse2 = createDownloadStaEventSubscriberWithExceptionThrown();
		eventManager.addEventSubscriber(dse1);
		eventManager.addEventSubscriber(dse2);
		DownloadStartedEvent eventToPush = new DownloadStartedEvent("test message");

		eventPublisher.publish(eventToPush);
		Thread.sleep(MILLIS);
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
