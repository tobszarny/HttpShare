package pl.biltech.httpshare.event;


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
