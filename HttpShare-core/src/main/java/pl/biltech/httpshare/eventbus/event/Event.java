package pl.biltech.httpshare.eventbus.event;


import pl.biltech.httpshare.eventbus.publisher.EventPublisher;
import pl.biltech.httpshare.eventbus.subscriber.EventSubscriber;

/**
 * Abstract for all published events. Events should be immutable
 * 
 * @see {@link EventPublisher}, {@link EventSubscriber}
 * 
 * @author bilu
 */
public interface Event {
}
