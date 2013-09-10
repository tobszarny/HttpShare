package pl.biltech.httpshare.event.impl;


import static pl.biltech.httpshare.util.Assert.isNotNull;

import java.util.ArrayList;
import java.util.List;

import pl.biltech.httpshare.event.Event;
import pl.biltech.httpshare.event.EventManager;
import pl.biltech.httpshare.event.EventPublisher;
import pl.biltech.httpshare.event.EventSubscriber;

/**
 * Simple singleton implementation with async publishing support
 * 
 * @author bilu
 */
public enum AsyncEventManager implements EventManager {

	INSTANCE;

	private final List<EventSubscriber<Event>> subscribers = new ArrayList<EventSubscriber<Event>>();
	private final List<EventPublisher> publishers = new ArrayList<EventPublisher>();


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
	public EventPublisher createEventPublisher() {
		EventPublisher publisher = new AsyncEventPublisher(this);
		publishers.add(publisher);
		return publisher;
	}

	@Override
	public void removeEventPublisher(EventPublisher eventPublisher) {
		isNotNull(eventPublisher);
		publishers.remove(eventPublisher);
	}

	List<EventSubscriber<Event>> getSubscribers() {
		return subscribers;
	}

}
