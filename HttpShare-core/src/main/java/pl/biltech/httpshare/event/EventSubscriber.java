package pl.biltech.httpshare.event;

/**
 * Simple subscriber mechanism. Allows to serve one particular event type
 * defined as generic parameter
 * 
 * @see {@link Event}, {@link EventPublisher}
 * 
 * @author bilu
 * 
 * @param <EVENT>
 *            defines type of handled event (subtype of {@link Event})
 */
public interface EventSubscriber<EVENT extends Event> {
	void handleEvent(EVENT event);
}
