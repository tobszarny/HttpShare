package pl.biltech.httpshare.server.support.impl;

import pl.biltech.httpshare.event.EventPublisher;
import pl.biltech.httpshare.server.support.HttpHandlerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static fi.iki.elonen.NanoHTTPD.Response;
import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;

public class NanoHttpHandlerFactory implements HttpHandlerFactory<Response> {
    private final EventPublisher eventPublisher;

    public NanoHttpHandlerFactory(EventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Response createRedirectHttpHandler(String redirectUrl) {
        return null;
    }

    @Override
    public Response createDownloadHttpHandler(File file) {
        return createDownloadHttpHandler(file, APPLICATION_OCTET_STREAM);
    }

    @Override
    public Response createDownloadHttpHandler(File file, String mime) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException ex) {
        }
        return newFixedLengthResponse(Response.Status.OK, mime, fis, file.length());
    }
}
