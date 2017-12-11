package pl.biltech.httpshare.server.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.biltech.httpshare.annotation.VisibleForTesting;
import pl.biltech.httpshare.event.EventPublisher;
import pl.biltech.httpshare.event.impl.DownloadWaitingForRequestEvent;
import pl.biltech.httpshare.httpd.NanoHTTPD;
import pl.biltech.httpshare.httpd.http.IHTTPSession;
import pl.biltech.httpshare.httpd.http.Method;
import pl.biltech.httpshare.httpd.http.Response;
import pl.biltech.httpshare.httpd.runner.impl.BoundRunner;
import pl.biltech.httpshare.httpd.socket.impl.IPServerSocketFactory;
import pl.biltech.httpshare.server.HttpShareServer;
import pl.biltech.httpshare.server.support.HttpHandlerFactory;
import pl.biltech.httpshare.server.support.impl.NanoHttpHandlerFactory;
import pl.biltech.httpshare.util.MimeUtil;
import pl.biltech.httpshare.util.NetworkUtil;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.Executors;

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
    private final HttpHandlerFactory<Response> httpHanderFactory;

    private java.util.List<NanoHTTPD> nanoHTTPDs = new ArrayList<>();


    public NanoHttpShareServer(EventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
        httpHanderFactory = new NanoHttpHandlerFactory(eventPublisher);
    }

    @VisibleForTesting
    NanoHttpShareServer(EventPublisher eventPublisher, HttpHandlerFactory httpHanderFactory) {
        this.eventPublisher = eventPublisher;
        this.httpHanderFactory = httpHanderFactory;
    }

    private String buildFileUrl(File file) throws UnknownHostException {
        StringBuilder urlBuilder = buildServerUrl();
        urlBuilder.append("/").append(file.getName());
        return urlBuilder.toString();
    }

    private StringBuilder buildServerUrl() throws UnknownHostException {
        StringBuilder urlBuilder = new StringBuilder("http://").append(networkUtil.getLocalHostName());
        if (this.port != 80) {
            urlBuilder.append(":").append(this.port);
        }
        return urlBuilder;
    }

    @Override
    public void stop() {
//        assertNotNull(nanoHTTPD);
        nanoHTTPDs.forEach(n -> n.stop());
//        nanoHTTPD.stop();
        cleanup();
    }

    private void cleanup() {
//        nanoHTTPD = null;
        nanoHTTPDs.clear();
        address = null;
        port = DEFAULT_START_PORT;
        url = null;

    }

    @Override
    public void addFileToDownload(File file) throws IOException {
        assertNotNull(file);
//        assertNotNull(nanoHTTPD);
        logger.debug("Adding file to download: {}", file.getAbsolutePath());

        String relativeDownloadPath = "/" + file.getName();
//        httpServer.createContext("/", httpHanderFactory.createRedirectHttpHandler(relativeDownloadPath));
//        httpServer.createContext(relativeDownloadPath, httpHanderFactory.createDownloadHttpHandler(file));

        url = buildFileUrl(file);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(new StringSelection(url), (clipboard1, contents) -> {
            // wywolane w momencie gdy ktos nadpisze schowek
        });
        eventPublisher.publish(new DownloadWaitingForRequestEvent(url));
    }

    @Override
    public void start() throws IOException {
        networkUtil = new NetworkUtil();
        port = networkUtil.findFirstFreePort(port);
        String localHostName = networkUtil.getLocalHostName();

        System.out.println(localHostName);

        String hostname = networkUtil.getLocalHostName();
        InetAddress[] allByName = InetAddress.getAllByName(hostname);

        InetAddress byName = InetAddress.getByName(hostname);

        Arrays.stream(allByName).forEach(inetAddress -> {
//        Arrays.stream(new InetAddress[]{byName}).forEach(inetAddress -> {
                    NanoHTTPD instance = new NanoHTTPD(inetAddress.getHostAddress(), port) {

                        @Override
                        public Response serve(IHTTPSession session) {
                            Method method = session.getMethod();
                            String uri = session.getUri();
                            Map<String, String> parms = session.getParms();
                            Map<String, String> headers = session.getHeaders();

                            try {
                                if ("/".equals(uri)) {
                                    return httpHanderFactory.createRedirectHttpHandler("/index.html");
                                } else if (uri.length() > 1) {
                                    if (uri.startsWith("/api")) {
                                        return httpHanderFactory.createJsonHttpHandler((Object) null);
                                    } else {
                                        String[] split = uri.split("/");
                                        String fileName = split[split.length - 1];
                                        logger.info(fileName);
                                        if ("favicon.ico".equalsIgnoreCase(fileName)) {
                                            return getFavicon();
                                        } else {
                                            return serveClientUIFiles(fileName);
                                        }
                                    }

                                }
                            } catch (Exception e) {
                                logger.error("Problem handling request", e);
                                return httpHanderFactory.createErrorHttpHandler(e);
                            }
                            return httpHanderFactory.createErrorHttpHandler("");
                        }

                        private Response serveClientUIFiles(String fileName) {
                            return httpHanderFactory.createFolderContentHttpHandler("dist", fileName);
                        }

                        private Response getFavicon() {
                            ClassLoader classLoader = getClass().getClassLoader();
                            File file = new File(classLoader.getResource("images/ico.png").getFile());
                            return httpHanderFactory.createDownloadHttpHandler(file, MimeUtil.IMAGE_PNG);
                        }
                    };
                    instance.setServerSocketFactory(new IPServerSocketFactory(port, inetAddress));
                    nanoHTTPDs.add(instance);
                }
        );

        nanoHTTPDs.stream().forEach(n -> {
            logger.info("Starting on " + n.getHostname() + ":" + n.getMyPort());
            try {
                n.setAsyncRunner(new BoundRunner(Executors.newFixedThreadPool(N_THREADS)));
                n.start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("Problem starting on " + n.getHostname(), e);
            }

        });

        logger.info("Running! Point your browsers to " + buildServerUrl() + " \n");

    }

    @Override
    public boolean isServerRunning() {
        return !nanoHTTPDs.isEmpty();
    }

    @Override
    public String getFileToDownloadUrl() {
//        assertNotNull(nanoHTTPD);
        return url;
    }

}
