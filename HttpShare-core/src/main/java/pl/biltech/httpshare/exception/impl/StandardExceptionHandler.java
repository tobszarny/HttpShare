package pl.biltech.httpshare.exception.impl;

import org.slf4j.Logger;
import pl.biltech.httpshare.eventbus.event.impl.ErrorEvent;
import pl.biltech.httpshare.eventbus.publisher.EventPublisher;
import pl.biltech.httpshare.exception.ExceptionHandler;

public class StandardExceptionHandler implements ExceptionHandler {

	private final Logger logger;
	private final EventPublisher eventPublisher;

	public StandardExceptionHandler(Logger logger, EventPublisher eventPublisher) {
		this.logger = logger;
		this.eventPublisher = eventPublisher;

	}

	@Override
	public void handle(String additionalMessage, Exception e) {
		logger.error(e.getMessage(), e);
		eventPublisher.publish(new ErrorEvent(additionalMessage + ": " + e.getMessage()
				+ ". For more details please check the log file."));

	}
}
