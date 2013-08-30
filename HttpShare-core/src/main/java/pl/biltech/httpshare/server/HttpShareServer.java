package pl.biltech.httpshare.server;

import static java.lang.String.format;
import static pl.biltech.httpshare.util.Assert.isNotNull;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.biltech.httpshare.annotation.VisibleForTesting;
import pl.biltech.httpshare.event.EventPublisher;
import pl.biltech.httpshare.event.impl.DownloadFinishedEvent;
import pl.biltech.httpshare.event.impl.DownloadStartedEvent;
import pl.biltech.httpshare.event.impl.DownloadWaitingForRequestEvent;
import pl.biltech.httpshare.event.impl.SimpleEventPublisher;
import pl.biltech.httpshare.view.util.NetworkUtil;
import pl.biltech.httpshare.view.util.StreamUtil;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

/**
 * 
 * TODO [bilu]: finish refactor, include already existing utils & create new one
 * 
 * @author bilu
 */
public class HttpShareServer {

	private static final Logger logger = LoggerFactory.getLogger(HttpShareServer.class);


	private int port = 80;
	private final boolean closeAfterFirstDownload = true;


	private NetworkUtil networkUtil;
	private InetSocketAddress address;


	private final EventPublisher eventPublisher;

	public HttpShareServer() {
		eventPublisher = SimpleEventPublisher.INSTANCE;
	}

	@VisibleForTesting
	HttpShareServer(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}


	public void start(File file) throws IOException {
		networkUtil = new NetworkUtil();
		port = networkUtil.findFirstFreePort(port);
		address = new InetSocketAddress(port);

		HttpServer server = HttpServer.create(address, 0);
		server.createContext("/", getRedirectHttpHandler("/" + file.getName()));
		server.createContext("/" + file.getName(), getDownloadFileHttpHandler(file, closeAfterFirstDownload));
		server.setExecutor(Executors.newCachedThreadPool());
		server.start();

		String url = buildUrl(file);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(new StringSelection(url), new ClipboardOwner() {
			@Override
			public void lostOwnership(Clipboard clipboard, Transferable contents) {
				// wywolane w momencie gdy ktos nadpisze schowek
			}
		});
		eventPublisher.publishAsync(new DownloadWaitingForRequestEvent(url));
	}

	private String buildUrl(File file) throws UnknownHostException {
		StringBuilder urlBuilder = new StringBuilder("http://").append(networkUtil.getLocalHostName());
		if (this.port != 80) {
			urlBuilder.append(":").append(this.port);
		}
		urlBuilder.append("/").append(file.getName());
		return urlBuilder.toString();
	}

	private HttpHandler getRedirectHttpHandler(final String redirectUrl) {
		return new HttpHandler() {

			@Override
			public void handle(HttpExchange exchange) throws IOException {
				exchange.getResponseHeaders().add("Location", redirectUrl);
				exchange.sendResponseHeaders(307, 0);
				exchange.getResponseBody().close();
			}
		};
	}

	private HttpHandler getDownloadFileHttpHandler(final File file, final boolean closeAfterFirstDownload) {
		return new HttpHandler() {

			@Override
			public void handle(HttpExchange exchange) throws IOException {

				String requestMethod = exchange.getRequestMethod();
				if (requestMethod.equalsIgnoreCase("GET")) {
					Headers responseHeaders = exchange.getResponseHeaders();
					responseHeaders.set("Content-Type", "application/octet-stream");
					responseHeaders.set("Content-Length", "" + file.length());
					exchange.sendResponseHeaders(200, 0);

					String message = format("Reciver %s [%s]", exchange.getRemoteAddress().getHostName(), exchange
							.getRemoteAddress().getAddress().getHostAddress());
					eventPublisher.publishAsync(new DownloadStartedEvent(message));

					OutputStream out = exchange.getResponseBody();
					InputStream in = new FileInputStream(file);
					StreamUtil.copyStream(in, out);

					// TODO incorporate in copy mechanism
					// eventPublisher.publish(new DownloadProgressEvent(32));

					eventPublisher.publishAsync(new DownloadFinishedEvent(message));
				}
			}
		};
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
