package pl.biltech.httpshare.eventbus.publisher;


import pl.biltech.httpshare.eventbus.event.Event;
import pl.biltech.httpshare.eventbus.manager.EventManager;
import pl.biltech.httpshare.eventbus.subscriber.EventSubscriber;

/**
 * Publish mechanism interface that allows simply publish events of all subtypes
 * of Event
 * 
 * @see {@link Event}, {@link EventSubscriber}
 * 
 * @author bilu
 */
public interface EventPublisher {

	/**
	 * Send event object to all interested subscribers subscribed by
	 * {@link EventManager}
	 * 
	 * @param event
	 *            - subtype of Event to be published
	 */
	void publish(Event event);

}
