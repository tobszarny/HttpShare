package pl.biltech.httpshare.event.impl;

import static pl.biltech.httpshare.util.Assert.isNotNull;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

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
	public void publish(Event event) {
		for (EventSubscriber<Event> subscriber : subscribers) {
			if (doesEventMatchSubscriber(event, subscriber)) {
				subscriber.handleEvent(event);
			}
		}
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
