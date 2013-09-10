package pl.biltech.httpshare.event;

/**
 * Subscriber mechanism. Allows to serve one particular event type defined as
 * generic parameter
 * 
 * @see {@link Event}, {@link EventPublisher}
 * 
 * @author bilu
 * 
 * @param <EVENT>
 *            defines type of handled event (subtype of {@link Event})
 */
public interface EventSubscriber<EVENT extends Event> {

	/**
	 * Handle event from all publishers created via {@link EventManager}
	 * 
	 * @param event
	 *            incoming event to handle
	 */
	void handleEvent(EVENT event);

}
