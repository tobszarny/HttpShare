package pl.biltech.httpshare.model;

import static java.lang.String.format;

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
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.concurrent.Executors;

import pl.biltech.httpshare.view.Tray;
import pl.biltech.httpshare.view.util.ImageUtil;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Download {

	private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;
	private final File file;
	private int port = 80;
	private final boolean closeAfterFirstDownload = true;
	private final Tray tray;

	public Download(File file, Tray tray) {
		this.file = file;
		this.tray = tray;
	}

	public void startServer() throws IOException {
		findFirstFreePort();
		InetSocketAddress address = new InetSocketAddress(port);
		HttpServer server = HttpServer.create(address, 0);
		server.createContext("/", getRedirectHttpHandler("/" + file.getName()));
		server.createContext("/" + file.getName(), getDownloadFileHttpHandler(file, closeAfterFirstDownload));
		server.setExecutor(Executors.newCachedThreadPool());
		server.start();
		String url = InetAddress.getLocalHost().getHostAddress() + ((port != 80) ? ":" + port : "") + "/"
		+ file.getName();
		tray.displayMessage("Server is waiting for download at "+url, "The url was copied to clipboard");
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(new StringSelection(url), new ClipboardOwner() {
			@Override
			public void lostOwnership(Clipboard clipboard, Transferable contents) {
				// wywolane w momencie gdy ktos nadpisze schowek
			}
		});
		tray.setStatus("Server is waiting for download at "+url);
		tray.setIcon(ImageUtil.createImageFromFilePath("/images/pause.png", "Choose file"));
	}

	private void findFirstFreePort() {

		//FIXME recurent
		ServerSocket socket = null;
		try {
		    socket = new ServerSocket(port);
		} catch (IOException e) {
			port++;
		    findFirstFreePort();
		} finally { 
		    // Clean up
		    if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
		    }
		}
		
	}

	private HttpHandler getRedirectHttpHandler(final String redirectUrl) {
		return new HttpHandler() {

			public void handle(HttpExchange exchange) throws IOException {
				exchange.getResponseHeaders().add("Location", redirectUrl);
				exchange.sendResponseHeaders(307, 0);
				exchange.getResponseBody().close();
			}
		};
	}

	private HttpHandler getDownloadFileHttpHandler(final File file, final boolean closeAfterFirstDownload) {
		return new HttpHandler() {

			public void handle(HttpExchange exchange) throws IOException {

				String requestMethod = exchange.getRequestMethod();
				if (requestMethod.equalsIgnoreCase("GET")) {
					Headers responseHeaders = exchange.getResponseHeaders();
					responseHeaders.set("Content-Type", "application/octet-stream");
					responseHeaders.set("Content-Length", "" + file.length());
					exchange.sendResponseHeaders(200, 0);
					
					String message = format("Reciver %s [%s]", exchange.getRemoteAddress().getHostName(),
							exchange.getRemoteAddress().getAddress().getHostAddress());
					tray.displayMessage("Download started", message);
					tray.setStatus("Downloading. "+message);
					tray.setIcon(ImageUtil.createImageFromFilePath("/images/uploading.png", "Choose file"));

					OutputStream out = exchange.getResponseBody();
					InputStream in = new FileInputStream(file);
					copyStream(out, in);

					System.out.println(message);
					if (closeAfterFirstDownload) {
						tray.displayMessage("Download finished", message);
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e) {
						}
						tray.exit();
					}
				}
			}

			private void copyStream(OutputStream out, InputStream in) throws IOException {
				byte[] bytes = new byte[DEFAULT_BUFFER_SIZE];
				int size;
				while ((size = in.read(bytes)) != -1)
					out.write(bytes, 0, size);
				out.flush();
				out.close();
				in.close();
			}
		};
	}
}
