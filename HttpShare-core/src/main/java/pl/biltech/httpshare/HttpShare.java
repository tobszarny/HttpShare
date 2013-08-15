package pl.biltech.httpshare;

import static pl.biltech.httpshare.util.Assert.isNotNull;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Facade for core functionalities
 * 
 * @author bilu
 * 
 */
public class HttpShare {

	private static final Logger logger = LoggerFactory.getLogger(HttpShare.class);

	public void start() {
		logger.error("Not yet implemented");
	}

	public boolean isStarted() {
		logger.error("Not yet implemented");
		return false;
	}

	public void stop() {
		logger.error("Not yet implemented");
	}

	public void setUploadDirectory(File directory) {
		logger.error("Not yet implemented");
	}

	public void addFileToDownload(File file) {
		isNotNull(file);
		logger.debug("Adding file to download: {}", file.getAbsolutePath());
		logger.error("Not yet implemented");
	}

	public String getDownloadUrl() {
		logger.error("Not yet implemented");
		return "Not yet implemented";
	}

	public void enableExitAfterDownload() {
		logger.error("Not yet implemented");
	}

	public void disableExitAfterDownload() {
		logger.error("Not yet implemented");
	}


}
