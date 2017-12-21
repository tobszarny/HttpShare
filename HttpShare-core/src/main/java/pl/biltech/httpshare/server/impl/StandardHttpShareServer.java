package pl.biltech.httpshare.server.impl;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.biltech.httpshare.annotation.VisibleForTesting;
import pl.biltech.httpshare.eventbus.event.impl.DownloadWaitingForRequestEvent;
import pl.biltech.httpshare.eventbus.publisher.EventPublisher;
import pl.biltech.httpshare.server.HttpShareServer;
import pl.biltech.httpshare.server.support.HttpHandlerFactory;
import pl.biltech.httpshare.server.support.impl.StandardHttpHandlerFactory;
import pl.biltech.httpshare.util.NetworkUtil;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Executors;

import static pl.biltech.httpshare.util.Assert.assertNotNull;

/**
 * 
 * TODO [bilu]: finish refactor, include already existing utils & create new one
 * 
 * @author bilu
 */
@SuppressWarnings("restriction")
public class StandardHttpShareServer implements HttpShareServer {

	private static final Logger logger = LoggerFactory.getLogger(StandardHttpShareServer.class);

	private static final int DEFAULT_START_PORT = 80;

	private int port = DEFAULT_START_PORT;

	private String url;
	private HttpServer httpServer;
	private NetworkUtil networkUtil;
	private InetSocketAddress address;

	private final EventPublisher eventPublisher;
	private final HttpHandlerFactory<HttpHandler> httpHanderFactory;

	public StandardHttpShareServer(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
		httpHanderFactory = new StandardHttpHandlerFactory(eventPublisher);
	}

	@VisibleForTesting
	StandardHttpShareServer(EventPublisher eventPublisher, HttpHandlerFactory httpHanderFactory) {
		this.eventPublisher = eventPublisher;
		this.httpHanderFactory = httpHanderFactory;
	}

	private String buildUrl(File file) throws UnknownHostException {
		StringBuilder urlBuilder = new StringBuilder("http://").append(networkUtil.getLocalHostName());
		if (this.port != 80) {
			urlBuilder.append(":").append(this.port);
		}
		urlBuilder.append("/").append(file.getName());
		return urlBuilder.toString();
	}

	@Override
	public void stop() {
		assertNotNull(httpServer);
		httpServer.stop(0);
		cleanup();
	}

	private void cleanup() {
		httpServer = null;
		address = null;
		port = DEFAULT_START_PORT;
		url = null;

	}

	@Override
	public void addFileToDownload(File file) throws IOException {
		assertNotNull(file);
		assertNotNull(httpServer);
		logger.debug("Adding file to download: {}", file.getAbsolutePath());

//		String relativeDownloadPath = "/" + file.getName();
//		httpServer.createContext("/", httpHanderFactory.createRedirectHttpHandler(relativeDownloadPath));
//		httpServer.createContext(relativeDownloadPath, httpHanderFactory.createDownloadHttpHandler(file));

		url = buildUrl(file);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(new StringSelection(url), new ClipboardOwner() {
			@Override
			public void lostOwnership(Clipboard clipboard, Transferable contents) {
				// wywolane w momencie gdy ktos nadpisze schowek
			}
		});
		eventPublisher.publish(new DownloadWaitingForRequestEvent(url));
	}

	@Override
	public void start() throws IOException {
		networkUtil = new NetworkUtil();
		port = networkUtil.findFirstFreePort(port);
		address = new InetSocketAddress(port);
		httpServer = HttpServer.create(address, 0);
		httpServer.setExecutor(Executors.newCachedThreadPool());
		httpServer.start();

	}

	@Override
	public boolean isServerRunning() {
		// FIXME quite naive verification
		return httpServer != null;
	}

	@Override
	public String getFileToDownloadUrl() {
		assertNotNull(httpServer);
		return url;
	}

}
