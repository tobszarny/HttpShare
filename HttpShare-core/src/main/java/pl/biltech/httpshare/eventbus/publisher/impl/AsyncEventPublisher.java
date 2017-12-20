package pl.biltech.httpshare.eventbus.publisher.impl;

import pl.biltech.httpshare.eventbus.event.Event;
import pl.biltech.httpshare.eventbus.manager.impl.AsyncEventManager;
import pl.biltech.httpshare.eventbus.publisher.EventPublisher;
import pl.biltech.httpshare.eventbus.subscriber.EventSubscriber;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static pl.biltech.httpshare.util.Assert.assertNotNull;

/**
 * Simple async publisher implementation
 *
 * @author bilu
 */
public class AsyncEventPublisher implements EventPublisher {

    private final AsyncEventManager asyncEventManager;

    public AsyncEventPublisher(AsyncEventManager asyncEventManager) {
        this.asyncEventManager = asyncEventManager;
    }

    @Override
    // TODO double check if concurrency is valid
    public void publish(final Event event) {
        assertNotNull(event);
        /*
         * It is not about many threads to be run. It is about avoid stopping
         * core application (also during subscriber side exception)
         */
        ExecutorService executor = Executors.newSingleThreadExecutor();
        for (final EventSubscriber<Event> subscriber : asyncEventManager.getSubscribers()) {
            if (subscriber.isEventAMatch(event)) {
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        subscriber.handleEvent(event);
                    }
                });
            }
        }
        executor.shutdown();

    }

}
