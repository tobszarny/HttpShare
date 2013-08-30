package pl.biltech.httpshare.event;

import java.util.List;

/**
 * Simple publish mechanism interface that allows add event subscribers.
 * Subscribers specifies what kind of event they are interested in
 * 
 * @see {@link Event}, {@link EventSubscriber}
 * 
 * @author bilu
 */
public interface EventPublisher {

	void publishSync(Event event);

	void publishAsync(Event event);

	void addEventSubscriber(EventSubscriber<? extends Event> subscriber);

	void removeEventSubscriber(EventSubscriber<? extends Event> subscriber);

	void addEventSubscribers(List<EventSubscriber<? extends Event>> subscribers);

	void removeEventSubscribers(List<EventSubscriber<? extends Event>> subscribers);
}
