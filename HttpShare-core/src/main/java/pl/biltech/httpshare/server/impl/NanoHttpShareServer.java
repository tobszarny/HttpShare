package pl.biltech.httpshare.server.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.biltech.httpshare.annotation.VisibleForTesting;
import pl.biltech.httpshare.eventbus.event.impl.DownloadWaitingForRequestEvent;
import pl.biltech.httpshare.eventbus.publisher.EventPublisher;
import pl.biltech.httpshare.httpd.NanoHTTPD;
import pl.biltech.httpshare.httpd.http.IHTTPSession;
import pl.biltech.httpshare.httpd.http.Method;
import pl.biltech.httpshare.httpd.http.Response;
import pl.biltech.httpshare.httpd.runner.impl.BoundRunner;
import pl.biltech.httpshare.httpd.socket.impl.IPServerSocketFactory;
import pl.biltech.httpshare.repository.FileRepository;
import pl.biltech.httpshare.repository.impl.FileRepositoryImpl;
import pl.biltech.httpshare.repository.model.FileItem;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

    private final EventPublisher eventPublisher;
    private final HttpHandlerFactory<Response> httpHanderFactory;

    private java.util.List<NanoHTTPD> nanoHTTPDs = new ArrayList<>();
    private String hostname;

    private final FileRepository fileRepository = new FileRepositoryImpl();

    public NanoHttpShareServer(EventPublisher eventPublisher) {
        this(eventPublisher, new NanoHttpHandlerFactory(eventPublisher));
    }

    @VisibleForTesting
    NanoHttpShareServer(EventPublisher eventPublisher, HttpHandlerFactory httpHanderFactory) {
        this.eventPublisher = eventPublisher;
        this.httpHanderFactory = httpHanderFactory;
        this.hostname = NetworkUtil.getLocalHostName();
    }

    private String buildFileUrl(File file) {
        StringBuilder urlBuilder = buildServerUrl();
        urlBuilder.append("/api/file/").append(file.getName());
        return urlBuilder.toString();
    }

    protected StringBuilder buildServerUrl() {
        StringBuilder urlBuilder = new StringBuilder("http://").append(this.hostname);
        if (this.port != 80) {
            urlBuilder.append(":").append(this.port);
        }
        return urlBuilder;
    }

    @Override
    public void stop() {
        nanoHTTPDs.forEach(n -> n.stop());
        cleanup();
    }

    private void cleanup() {
        nanoHTTPDs.clear();
        port = DEFAULT_START_PORT;
        url = null;

    }

    @Override
    public void addFileToDownload(File file) throws IOException {
        assertNotNull(file);
        logger.debug("Adding file to download: {}", file.getAbsolutePath());

        FileItem fileItem = new FileItem()
                .withPersistentDownload(false)
                .withRemovable(false)
                .withServerUrl(buildServerUrl().toString())
                .withFile(file);

        fileRepository.add(fileItem);


        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(new StringSelection(fileItem.getUrl()), (clipboard1, contents) -> {
            // wywolane w momencie gdy ktos nadpisze schowek
            logger.warn("Lost ownership of clipboard");
        });
        eventPublisher.publish(new DownloadWaitingForRequestEvent(fileItem.getUrl()));
    }

    @Override
    public void start() throws IOException {
        port = NetworkUtil.findFirstFreePort(port);

        InetAddress[] allByName = InetAddress.getAllByName(hostname);

        Arrays.stream(allByName)
                .filter(f -> f.isSiteLocalAddress())
                .forEach(inetAddress -> {
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
                                            String[] split = uri.split("/");
                                            if ("api".equals(split[1])) {
                                                if ("file".equals(split[2])) {
                                                    if (split.length == 3) {
                                                        logger.info("ShowAllFiles");
                                                        List<FileItem> all = this.getFileRepository().getAll();
                                                        return httpHanderFactory.createJsonHttpHandler(all);
                                                    } else {
                                                        return httpHanderFactory.createJsonHttpHandler((Object) null);
                                                    }
                                                } else {
                                                    return httpHanderFactory.createJsonHttpHandler((Object) null);
                                                }
                                            } else {
                                                String fileName = split[split.length - 1];
                                                logger.info("{}:{}/{}", this.getHostname(), this.getMyPort(), fileName);
                                                if ("favicon.ico".equalsIgnoreCase(fileName)) {
                                                    return getFavicon();
                                                } else if ("config.js".equalsIgnoreCase(fileName)) {
                                                    return httpHanderFactory.createJsonHttpHandler("window.CONFIG = { apiUrl:'" + buildServerUrl() + "'};");
                                                } else {
                                                    return serveClientUIFiles(fileName);
                                                }
                                            }

                                        }
                                    } catch (Exception e) {
                                        logger.error("Problem handling request " + uri, e);
                                        return httpHanderFactory.createErrorHttpHandler(e);
                                    }
                                    return httpHanderFactory.createErrorHttpHandler("");
                                }

                                private Response serveClientUIFiles(String fileName) {
                                    return httpHanderFactory.createFolderContentHttpHandler("C:\\git-ws\\prv\\HttpShare\\HttpShare-client-gui\\dist", fileName);
                                }

                                private Response serveResourceClientUIFiles(String fileName) {
                                    return httpHanderFactory.createResourceFolderContentHttpHandler("dist", fileName);
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
                n.setFileRepository(fileRepository);
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
        return url;
    }

}
