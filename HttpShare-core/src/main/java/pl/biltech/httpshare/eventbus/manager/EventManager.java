package pl.biltech.httpshare.eventbus.manager;


import pl.biltech.httpshare.eventbus.event.Event;
import pl.biltech.httpshare.eventbus.publisher.EventPublisher;
import pl.biltech.httpshare.eventbus.subscriber.EventSubscriber;

/**
 * Event management facade
 * 
 * @author bilu
 */
public interface EventManager {

	void addEventSubscriber(EventSubscriber<? extends Event> subscriber);

	void removeEventSubscriber(EventSubscriber<? extends Event> subscriber);

	EventPublisher createEventPublisher();

	void removeEventPublisher(EventPublisher eventPublisher);

}
