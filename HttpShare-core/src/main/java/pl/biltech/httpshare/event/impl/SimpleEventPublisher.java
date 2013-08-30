package pl.biltech.httpshare.event.impl;

import static pl.biltech.httpshare.util.Assert.isNotNull;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pl.biltech.httpshare.event.Event;
import pl.biltech.httpshare.event.EventPublisher;
import pl.biltech.httpshare.event.EventSubscriber;

/**
 * Simple singleton implementation.
 * 
 * @author bilu
 */
public enum SimpleEventPublisher implements EventPublisher {

	INSTANCE;

	private final List<EventSubscriber<Event>> subscribers = new ArrayList<EventSubscriber<Event>>();

	@Override
	public void publishSync(Event event) {
		for (EventSubscriber<Event> subscriber : subscribers) {
			if (doesEventMatchSubscriber(event, subscriber)) {
				subscriber.handleEvent(event);
			}
		}
	}

	@Override
	// TODO double check if concurency is valid
	public void publishAsync(final Event event) {
		// It is not about many threads to be run. It is about avoid stopping
		// core application during subscriber side exception
		ExecutorService executor = Executors.newSingleThreadExecutor();
		for (final EventSubscriber<Event> subscriber : subscribers) {
			if (doesEventMatchSubscriber(event, subscriber)) {
				executor.execute(new Runnable() {
					@Override
					public void run() {
						subscriber.handleEvent(event);
					}
				});
			}
		}
		executor.shutdown();

	}

	private boolean doesEventMatchSubscriber(Event event, EventSubscriber<Event> subscriber) {
		return ((ParameterizedType) subscriber.getClass().getGenericInterfaces()[0]).getActualTypeArguments()[0]
				.equals(event.getClass());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void addEventSubscriber(EventSubscriber subscriber) {
		isNotNull(subscriber);
		subscribers.add(subscriber);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void removeEventSubscriber(EventSubscriber subscriber) {
		isNotNull(subscriber);
		subscribers.remove(subscriber);
	}

	@Override
	public void addEventSubscribers(List<EventSubscriber<? extends Event>> subscribers) {
		for (EventSubscriber<? extends Event> eventSubscriber : subscribers) {
			addEventSubscriber(eventSubscriber);
		}
	}

	@Override
	public void removeEventSubscribers(List<EventSubscriber<? extends Event>> subscribers) {
		for (EventSubscriber<? extends Event> eventSubscriber : subscribers) {
			removeEventSubscriber(eventSubscriber);
		}
	}

}
