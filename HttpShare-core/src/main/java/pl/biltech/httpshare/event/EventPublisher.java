package pl.biltech.httpshare.event;


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
