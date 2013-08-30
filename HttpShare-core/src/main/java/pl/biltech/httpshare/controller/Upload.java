package pl.biltech.httpshare.controller;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Executors;

import org.apache.commons.fileupload.MultipartStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.biltech.httpshare.model.HttpStatusCode;
import pl.biltech.httpshare.model.HttpMethod;
import pl.biltech.httpshare.view.Tray;
import pl.biltech.httpshare.view.util.HttpUtil;
import pl.biltech.httpshare.view.util.ImageUtil;
import pl.biltech.httpshare.view.util.NetworkUtil;
import pl.biltech.httpshare.view.util.StreamUtil;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

/**
 * @author tomek
 * 
 */
@SuppressWarnings("restriction")
public class Upload {

	private static final Logger logger = LoggerFactory.getLogger(Upload.class);

	
	// FIXME: get upload page from the resources
	private static final String UPLOAD_PAGE = "<form enctype=\"multipart/form-data\" action=\"@@URL@@\" method=\"POST\">\r\n"
			+ "<input type=\"hidden\" name=\"MAX_FILE_SIZE\" value=\"100000\" />\r\n"
			+ "Choose a file to upload: <input name=\"uploadedfile\" type=\"file\" /><br />\r\n"
			+ "<input type=\"submit\" value=\"Upload File\" />\r\n" + "</form>";

	private static final String UPLOAD_SUCCESS = "<div><H3>File @@FILE@@ uploaded sucessfuly</H3></div>";
	private static final String UPLOAD_FAILURE = "<div><H3>File @@FILE@@ upload failed</H3><div>@@ERROR@@</div></div>";

	private File folder;
	private final Tray tray;
	private int port = 80;

	private NetworkUtil networkUtil;

	public Upload(File folder, Tray tray) {
		this.folder = folder;
		this.tray = tray;
	}

	public void startServer() throws IOException {
		logger.debug("startServer called");

		networkUtil = new NetworkUtil();

		port = networkUtil.findFirstFreePort(port);

		InetSocketAddress address = new InetSocketAddress(port);
		HttpServer server = HttpServer.create(address, 0);
		server.createContext("/",
				getRedirectHttpHandler("/" + folder.getName()));
		server.createContext("/" + this.folder.getName(),
				getUploadFileHttpHandler(folder, true));

		server.createContext("/" + folder.getName() + "/upload",
				getUploadFileHttpHandler(folder, true));

		server.setExecutor(Executors.newCachedThreadPool());
		server.start();

		String url = buildUrl();

		this.tray.displayMessage("Server is waiting for upload at " + url,
				"The url was copied to clipboard");
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(new StringSelection(url), new ClipboardOwner() {
			public void lostOwnership(Clipboard clipboard, Transferable contents) {
			}
		});
		this.tray.setStatus("Server is waiting for upload at " + url);
		this.tray.setIcon(ImageUtil.createImageFromFilePath(
				"/images/pause.png", "Choose file"));

		try {
			Thread.sleep(30000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		logger.info("Shutting down!");
		System.exit(0);
	}

	private String buildUrl() throws UnknownHostException {
		StringBuilder urlBuilder = new StringBuilder("http://")
				.append(networkUtil.getLocalHostName());
		if (this.port != 80) {
			urlBuilder.append(":").append(this.port);
		}
		urlBuilder.append("/").append(this.folder.getName());
		return urlBuilder.toString();
	}

	private HttpHandler getRedirectHttpHandler(final String redirectUrl) {
		logger.debug("getRedirectHttpHandler called");
		return new HttpHandler() {
			public void handle(HttpExchange exchange) throws IOException {
				exchange.getResponseHeaders().add("Location", redirectUrl);
				exchange.sendResponseHeaders(307, 0L);
				exchange.getResponseBody().close();
			}
		};
	}

	private HttpHandler getUploadFileHttpHandler(final File folder,
			final boolean closeAfterFirstUpload) {
		logger.debug("getUploadFileHttpHandler called");
		return new HttpHandler() {

			@SuppressWarnings("unused")
			public void handle(HttpExchange exchange) throws IOException {
				logger.debug("handle called");
				logger.info("Context = " + exchange.getHttpContext().getPath());
				String requestMethod = exchange.getRequestMethod();
				logger.info("HTTP method = " + requestMethod);
				if (requestMethod.equalsIgnoreCase(HttpMethod.GET.name())) {

					Headers responseHeaders = exchange.getResponseHeaders();
					responseHeaders.set("Content-Type", "text/html");
					responseHeaders.set("Content-Length",
							Long.toString(UPLOAD_PAGE.length()));
					exchange.sendResponseHeaders(
							HttpStatusCode.ACCEPTED.getCode(), 0L);

					String message = String.format("Reciever %s [%s]",
							new Object[] {
									exchange.getRemoteAddress().getHostName(),
									exchange.getRemoteAddress().getAddress()
											.getHostAddress() });
					Upload.this.tray.displayMessage("Upload started", message);
					Upload.this.tray.setStatus("Uploading. " + message);
					Upload.this.tray.setIcon(ImageUtil.createImageFromFilePath(
							"/images/uploading.png", "Choose file"));

					OutputStream out = exchange.getResponseBody();
					InputStream in = new ByteArrayInputStream(UPLOAD_PAGE
							.replaceFirst("@@URL@@",
									folder.getName() + "/upload").getBytes(
									"UTF-8"));// new
												// FileInputStream(file);
					StreamUtil.copyStream(in, out);

					// System.out.println(message);
					if (closeAfterFirstUpload) {
						Upload.this.tray.displayMessage("Uploading finished",
								message);
						try {
							Thread.sleep(5000L);
						} catch (InterruptedException localInterruptedException) {
						}
						logger.info("Exiting!");
						Upload.this.tray.exit();
					}
				} else if (requestMethod.equalsIgnoreCase(HttpMethod.POST.name())) {
					logger.info("POST caught");

					Exception exception = null;

					String fileName = null;

					Headers requestHeaders = exchange.getRequestHeaders();

					String boundary = HttpUtil.getBoundary(requestHeaders);

					InputStream requestBody = exchange.getRequestBody();

					@SuppressWarnings("deprecation")
					MultipartStream multipartStream = new MultipartStream(
							requestBody, boundary.getBytes());

					boolean nextPart = multipartStream.skipPreamble();
					while (nextPart) {
						String header = multipartStream.readHeaders();
						System.out.println("");
						System.out.println("Headers:");
						System.out.println(header);

						fileName = HttpUtil
								.getContentDispositionFilename(header);

						if (!fileName.isEmpty()) {

							try {

								logger.debug("Got file Filename = " + fileName);

								File file = new File(folder.getAbsolutePath(),
										fileName);

								logger.debug("FileLocation = "
										+ file.getAbsolutePath());

								if (file.exists()) {
									file.delete();
								} else {
									file.mkdirs();
								}

								FileOutputStream fos = new FileOutputStream(
										file);
								int bytes = multipartStream.readBodyData(fos);

								logger.debug("Read bytes " + bytes);

								try {
									fos.flush();
								} finally {
									fos.close();
								}

							} catch (Exception e) {
								e.printStackTrace();
								exception = e;
								throw new RuntimeException(e);
							}

						} else {

							logger.debug("No file");

							multipartStream.discardBodyData();
						}

						System.out.println("");
						nextPart = multipartStream.readBoundary();
					} // eof while

					String path = "somePath";

					httpRedirect(exchange, path);

					return;

					/*
					 * if (exception == null) {
					 * 
					 * String escapedHtml = UPLOAD_SUCCESS.replaceFirst(
					 * "@@FILE@@", fileName).replaceFirst( "@@ERROR@@",
					 * StringEscapeUtils.escapeHtml(exception .getMessage()));
					 * 
					 * Headers responseHeaders = exchange.getResponseHeaders();
					 * responseHeaders.set("Content-Type", "text/html");
					 * responseHeaders.set("Content-Length",
					 * Long.toString(escapedHtml.length()));
					 * exchange.sendResponseHeaders(200, 0L);
					 * 
					 * OutputStream responseBody = exchange.getResponseBody();
					 * 
					 * InputStream in = new ByteArrayInputStream(
					 * escapedHtml.getBytes("UTF-8"));// new //
					 * FileInputStream(file); StreamUtil.copyStream(in,
					 * responseBody);
					 * 
					 * try { responseBody.flush(); } finally {
					 * 
					 * responseBody.close(); }
					 * 
					 * try { in.close(); } finally {
					 * 
					 * }
					 * 
					 * } else { Headers responseHeaders =
					 * exchange.getResponseHeaders();
					 * responseHeaders.set("Content-Type", "text/html");
					 * responseHeaders.set("Content-Length",
					 * Long.toString(UPLOAD_PAGE.length()));
					 * exchange.sendResponseHeaders(501, 0L);
					 * 
					 * OutputStream responseBody = exchange.getResponseBody();
					 * 
					 * String escapedHtml = UPLOAD_FAILURE.replaceFirst(
					 * "@@FILE@@", fileName).replaceFirst( "@@ERROR@@",
					 * StringEscapeUtils.escapeHtml(exception .getMessage()));
					 * 
					 * InputStream in = new ByteArrayInputStream(
					 * escapedHtml.getBytes("UTF-8"));// new //
					 * FileInputStream(file); StreamUtil.copyStream(in,
					 * responseBody);
					 * 
					 * try { responseBody.flush(); } finally {
					 * 
					 * responseBody.close(); }
					 * 
					 * try { in.close(); } finally {
					 * 
					 * } }
					 */

				}
			}

			private void httpRedirect(HttpExchange exchange, String path)
					throws IOException {
				exchange.getResponseHeaders().add("Location", path);
				exchange.sendResponseHeaders(
						HttpStatusCode.MOVED_PERMANENTLY.getCode(), 0);
				exchange.getResponseBody().close();
			}

		};
	}

}
