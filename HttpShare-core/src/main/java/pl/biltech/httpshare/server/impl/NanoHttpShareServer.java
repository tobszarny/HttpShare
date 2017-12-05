package pl.biltech.httpshare.server.impl;

import fi.iki.elonen.NanoHTTPD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.biltech.httpshare.annotation.VisibleForTesting;
import pl.biltech.httpshare.event.EventPublisher;
import pl.biltech.httpshare.event.impl.DownloadWaitingForRequestEvent;
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.Executors;

import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;
import static pl.biltech.httpshare.util.Assert.assertNotNull;

/**
 * @author tomasz
 */
@SuppressWarnings("restriction")
public class NanoHttpShareServer implements HttpShareServer {

    private static final Logger logger = LoggerFactory.getLogger(NanoHttpShareServer.class);

    private static final int DEFAULT_START_PORT = 80;
    public static final int N_THREADS = 10;

    private int port = DEFAULT_START_PORT;

    private String url;
    private NetworkUtil networkUtil;
    private InetSocketAddress address;

    private final EventPublisher eventPublisher;
    private final HttpHandlerFactory httpHanderFactory;

    private NanoHTTPD nanoHTTPD;


    public NanoHttpShareServer(EventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
        httpHanderFactory = new StandardHttpHandlerFactory(eventPublisher);
    }

    @VisibleForTesting
    NanoHttpShareServer(EventPublisher eventPublisher, HttpHandlerFactory httpHanderFactory) {
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
        assertNotNull(nanoHTTPD);
        nanoHTTPD.stop();
        cleanup();
    }

    private void cleanup() {
        nanoHTTPD = null;
        address = null;
        port = DEFAULT_START_PORT;
        url = null;

    }

    @Override
    public void addFileToDownload(File file) throws IOException {
        assertNotNull(file);
        assertNotNull(nanoHTTPD);
        logger.debug("Adding file to download: {}", file.getAbsolutePath());

        String relativeDownloadPath = "/" + file.getName();
//        httpServer.createContext("/", httpHanderFactory.createRedirectHttpHandler(relativeDownloadPath));
//        httpServer.createContext(relativeDownloadPath, httpHanderFactory.createDownloadHttpHandler(file));

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
        String localHostName = networkUtil.getLocalHostName();

        nanoHTTPD = new NanoHTTPD(localHostName, port) {
            @Override
            public Response serve(IHTTPSession session) {
                Method method = session.getMethod();
                String uri = session.getUri();
                Map<String, String> parms = session.getParms();

                String msg = "<html><body><h1>Hello server</h1>\n";
                if (parms.get("username") == null) {
                    msg += "<form action='?' method='get'>\n  <p>Your name: <input type='text' name='username'></p>\n" + "</form>\n";
                } else {
                    msg += "<p>Hello, " + parms.get("username") + "!</p>";
                }
                return newFixedLengthResponse(msg + "</body></html>\n");
            }
        };

        nanoHTTPD.setAsyncRunner(new BoundRunner(Executors.newFixedThreadPool(N_THREADS)));

        nanoHTTPD.start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        logger.info("Running! Point your browsers to http://" + localHostName + ":" + port + "/ \n");


//        address = new InetSocketAddress(port);
//        httpServer = HttpServer.create(address, 0);
//        httpServer.setExecutor(Executors.newCachedThreadPool());
//        httpServer.start();

    }

    private NanoHTTPD.Response downloadFile(File file) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException ex) {
        }
        return newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "application/octet-stream", fis, file.getTotalSpace());
    }

    @Override
    public boolean isServerRunning() {
        return nanoHTTPD != null && nanoHTTPD.isAlive();
    }

    @Override
    public String getFileToDownloadUrl() {
        assertNotNull(nanoHTTPD);
        return url;
    }

}
