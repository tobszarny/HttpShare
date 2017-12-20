package pl.biltech.httpshare.eventbus.manager.impl;


import pl.biltech.httpshare.eventbus.event.Event;
import pl.biltech.httpshare.eventbus.manager.EventManager;
import pl.biltech.httpshare.eventbus.publisher.EventPublisher;
import pl.biltech.httpshare.eventbus.publisher.impl.AsyncEventPublisher;
import pl.biltech.httpshare.eventbus.subscriber.EventSubscriber;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static pl.biltech.httpshare.util.Assert.assertNotNull;

/**
 * Simple singleton implementation with async publishing support
 *
 * @author bilu
 */
public enum AsyncEventManager implements EventManager {

    INSTANCE;

    private final List<EventSubscriber<Event>> subscribers = new ArrayList<EventSubscriber<Event>>();
    private final List<EventPublisher> publishers = new ArrayList<EventPublisher>();


    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public void addEventSubscriber(EventSubscriber subscriber) {
        assertNotNull(subscriber);
        subscribers.add(subscriber);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void removeEventSubscriber(EventSubscriber subscriber) {
        assertNotNull(subscriber);
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
        assertNotNull(eventPublisher);
        publishers.remove(eventPublisher);
    }

    public List<EventSubscriber<Event>> getSubscribers() {
        return Collections.unmodifiableList(subscribers);
    }

}
