package pl.biltech.httpshare.event.impl;

import static pl.biltech.httpshare.util.Assert.isNotNull;

import java.lang.reflect.ParameterizedType;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pl.biltech.httpshare.event.Event;
import pl.biltech.httpshare.event.EventPublisher;
import pl.biltech.httpshare.event.EventSubscriber;

/**
 * Simple async publisher implementation
 * 
 * @author bilu
 */
class AsyncEventPublisher implements EventPublisher {

	private final AsyncEventManager asyncEventManager;

	public AsyncEventPublisher(AsyncEventManager asyncEventManager) {
		this.asyncEventManager = asyncEventManager;
	}

	@Override
	// TODO double check if concurency is valid
	public void publish(final Event event) {
		isNotNull(event);
		/*
		 * It is not about many threads to be run. It is about avoid stopping
		 * core application (also during subscriber side exception)
		 */
		ExecutorService executor = Executors.newSingleThreadExecutor();
		for (final EventSubscriber<Event> subscriber : asyncEventManager.getSubscribers()) {
			if (doesEventMatchSubscriber(event, subscriber)) {
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

	private boolean doesEventMatchSubscriber(Event event, EventSubscriber<Event> subscriber) {
		return ((ParameterizedType) subscriber.getClass().getGenericInterfaces()[0]).getActualTypeArguments()[0]
				.equals(event.getClass());
	}

}
