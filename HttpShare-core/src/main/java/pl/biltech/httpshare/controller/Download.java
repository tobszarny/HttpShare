//package pl.biltech.httpshare.controller;
//
//import static java.lang.String.format;
//
//import java.awt.Toolkit;
//import java.awt.datatransfer.Clipboard;
//import java.awt.datatransfer.ClipboardOwner;
//import java.awt.datatransfer.StringSelection;
//import java.awt.datatransfer.Transferable;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.net.InetSocketAddress;
//import java.net.UnknownHostException;
//import java.util.concurrent.Executors;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import pl.biltech.httpshare.view.Tray;
//import pl.biltech.httpshare.view.util.NetworkUtil;
//import pl.biltech.httpshare.view.util.StreamUtil;
//
//import com.sun.net.httpserver.Headers;
//import com.sun.net.httpserver.HttpExchange;
//import com.sun.net.httpserver.HttpHandler;
//import com.sun.net.httpserver.HttpServer;
//
///**
// * @author bilu, tomek
// * 
// */
//@SuppressWarnings("restriction")
//public class Download {
//
//	private static final Logger logger = LoggerFactory.getLogger(Download.class);
//
//	private final File file;
//	private int port = 80;
//	private final boolean closeAfterFirstDownload = true;
//	private final Tray tray;
//
//	private NetworkUtil networkUtil;
//
//	public Download(File file, Tray tray) {
//		this.file = file;
//		this.tray = tray;
//	}
//
//	public void startServer() throws IOException {
//		networkUtil = new NetworkUtil();
//		port = networkUtil.findFirstFreePort(port);
//		InetSocketAddress address = new InetSocketAddress(port);
//
//		HttpServer server = HttpServer.create(address, 0);
//		server.createContext("/", getRedirectHttpHandler("/" + file.getName()));
//		server.createContext("/" + file.getName(), getDownloadFileHttpHandler(file, closeAfterFirstDownload));
//		server.setExecutor(Executors.newCachedThreadPool());
//		server.start();
//
//		String url = buildUrl();
//		tray.displayMessage("Server is waiting for download at " + url, "The url was copied to clipboard");
//		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
//		clipboard.setContents(new StringSelection(url), new ClipboardOwner() {
//			@Override
//			public void lostOwnership(Clipboard clipboard, Transferable contents) {
//				// wywolane w momencie gdy ktos nadpisze schowek
//			}
//		});
//		tray.setStatus("Server is waiting for download at " + url);
//		tray.setIcon(ImageUtil.createImageFromFilePath("/images/pause.png", "Choose file"));
//	}
//
//	private String buildUrl() throws UnknownHostException {
//		StringBuilder urlBuilder = new StringBuilder("http://").append(networkUtil.getLocalHostName());
//		if (this.port != 80) {
//			urlBuilder.append(":").append(this.port);
//		}
//		urlBuilder.append("/").append(this.file.getName());
//		return urlBuilder.toString();
//	}
//
//	private HttpHandler getRedirectHttpHandler(final String redirectUrl) {
//		return new HttpHandler() {
//
//			@Override
//			public void handle(HttpExchange exchange) throws IOException {
//				exchange.getResponseHeaders().add("Location", redirectUrl);
//				exchange.sendResponseHeaders(307, 0);
//				exchange.getResponseBody().close();
//			}
//		};
//	}
//
//	private HttpHandler getDownloadFileHttpHandler(final File file, final boolean closeAfterFirstDownload) {
//		return new HttpHandler() {
//
//			@Override
//			public void handle(HttpExchange exchange) throws IOException {
//
//				String requestMethod = exchange.getRequestMethod();
//				if (requestMethod.equalsIgnoreCase("GET")) {
//					Headers responseHeaders = exchange.getResponseHeaders();
//					responseHeaders.set("Content-Type", "application/octet-stream");
//					responseHeaders.set("Content-Length", "" + file.length());
//					exchange.sendResponseHeaders(200, 0);
//
//					String message = format("Reciver %s [%s]", exchange.getRemoteAddress().getHostName(), exchange
//							.getRemoteAddress().getAddress().getHostAddress());
//					tray.displayMessage("Download started", message);
//					tray.setStatus("Downloading. " + message);
//					tray.setIcon(ImageUtil.createImageFromFilePath("/images/uploading.png", "Choose file"));
//
//					OutputStream out = exchange.getResponseBody();
//					InputStream in = new FileInputStream(file);
//					StreamUtil.copyStream(in, out);
//
//					System.out.println(message);
//					if (closeAfterFirstDownload) {
//						tray.displayMessage("Download finished", message);
//						try {
//							Thread.sleep(5000);
//						} catch (InterruptedException e) {
//						}
//						tray.exit();
//					}
//				}
//			}
//		};
//	}
// }
