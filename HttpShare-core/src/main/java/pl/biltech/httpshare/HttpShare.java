package pl.biltech.httpshare;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.biltech.httpshare.event.EventPublisher;
import pl.biltech.httpshare.event.impl.AsyncEventManager;
import pl.biltech.httpshare.exception.ExceptionHandler;
import pl.biltech.httpshare.exception.impl.StandardExceptionHandler;
import pl.biltech.httpshare.server.HttpShareServer;
import pl.biltech.httpshare.server.impl.NanoHttpShareServer;
import pl.biltech.httpshare.server.impl.StandardHttpShareServer;

import java.io.File;
import java.io.IOException;

import static pl.biltech.httpshare.util.Assert.assertNotNull;

/**
 * Facade for core functionalities
 * 
 * @author bilu
 * 
 */
public class HttpShare {

	private static final Logger logger = LoggerFactory.getLogger(HttpShare.class);
	private final EventPublisher eventPublisher = AsyncEventManager.INSTANCE.createEventPublisher();
	private final ExceptionHandler exceptionHandler = new StandardExceptionHandler(logger, eventPublisher);
	private HttpShareServer httpShareServer;

	public void start() {
		try {
			httpShareServer = new StandardHttpShareServer(eventPublisher);
			httpShareServer = new NanoHttpShareServer(eventPublisher);
			httpShareServer.start();
		} catch (IOException e) {
			exceptionHandler.handle("Error during HttpShareServer startup", e);
		}
	}

	public boolean isStarted() {
		return httpShareServer != null && httpShareServer.isServerRunning();
	}

	public void stop() {
		httpShareServer.stop();
	}

	public void addFileToDownload(File file) {
		assertNotNull(file);
		logger.debug("Adding file to download: {}", file.getAbsolutePath());
		try {
			httpShareServer.addFileToDownload(file);
		} catch (IOException e) {
			exceptionHandler.handle("Error during file upload", e);
		}
	}

	public String getDownloadUrl() {
		return httpShareServer.getFileToDownloadUrl();
	}

}
