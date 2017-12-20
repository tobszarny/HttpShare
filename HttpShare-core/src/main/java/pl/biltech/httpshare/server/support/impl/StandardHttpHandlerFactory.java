package pl.biltech.httpshare.server.support.impl;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import pl.biltech.httpshare.eventbus.event.impl.DownloadFinishedEvent;
import pl.biltech.httpshare.eventbus.event.impl.DownloadStartedEvent;
import pl.biltech.httpshare.eventbus.publisher.EventPublisher;
import pl.biltech.httpshare.model.HttpStatusCode;
import pl.biltech.httpshare.server.support.HttpHandlerFactory;
import pl.biltech.httpshare.util.MimeUtil;
import pl.biltech.httpshare.util.StreamUtil;

import java.io.*;

import static java.lang.String.format;
import static pl.biltech.httpshare.model.HttpStatusCode.TEMPORARY_REDIRECT_HTTP_1_1;

@SuppressWarnings("restriction")
public class StandardHttpHandlerFactory implements HttpHandlerFactory<HttpHandler> {

    private final EventPublisher eventPublisher;

    public StandardHttpHandlerFactory(EventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public HttpHandler createRedirectHttpHandler(final String redirectUrl) {
        return new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                exchange.getResponseHeaders().add("Location", redirectUrl);
                exchange.sendResponseHeaders(TEMPORARY_REDIRECT_HTTP_1_1.getCode(), 0);
                exchange.getResponseBody().close();
            }
        };

    }

    @Override
    public HttpHandler createDownloadHttpHandler(final File file) {
        return createDownloadHttpHandler(file, MimeUtil.APPLICATION_OCTET_STREAM);
    }

    @Override
    public HttpHandler createDownloadHttpHandler(File file, String mime) {
        return new HttpHandler() {

            @Override
            public void handle(HttpExchange exchange) throws IOException {

                String requestMethod = exchange.getRequestMethod();
                if (requestMethod.equalsIgnoreCase("GET")) {
                    Headers responseHeaders = exchange.getResponseHeaders();
                    responseHeaders.set("Content-Type", mime);
                    responseHeaders.set("Content-Length", "" + file.length());
                    exchange.sendResponseHeaders(HttpStatusCode.ACCEPTED.getCode(), 0);

                    String message = format("Reciver %s [%s]", exchange.getRemoteAddress().getHostName(), exchange
                            .getRemoteAddress().getAddress().getHostAddress());
                    eventPublisher.publish(new DownloadStartedEvent(message));

                    OutputStream out = exchange.getResponseBody();
                    InputStream in = new FileInputStream(file);
                    StreamUtil.copyStream(in, out);

                    // TODO incorporate in copy mechanism
                    // eventPublisher.publish(new DownloadProgressEvent(32));

                    eventPublisher.publish(new DownloadFinishedEvent(message));
                }
            }
        };
    }

    @Override
    public HttpHandler createErrorHttpHandler(Exception e) {
        return null;
    }

    @Override
    public HttpHandler createErrorHttpHandler(String message) {
        return null;
    }

    @Override
    public HttpHandler createJsonHttpHandler(String json) {
        return null;
    }

    @Override
    public HttpHandler createJsonHttpHandler(Object object) throws Exception {
        return null;
    }

    @Override
    public HttpHandler createFolderContentHttpHandler(String folder, String fileName) {
        return null;
    }
}
