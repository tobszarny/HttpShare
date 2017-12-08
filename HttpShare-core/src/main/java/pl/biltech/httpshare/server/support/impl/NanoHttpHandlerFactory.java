package pl.biltech.httpshare.server.support.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.exception.ExceptionUtils;
import pl.biltech.httpshare.event.EventPublisher;
import pl.biltech.httpshare.httpd.Response;
import pl.biltech.httpshare.httpd.ResponseStatus;
import pl.biltech.httpshare.repository.model.FileItem;
import pl.biltech.httpshare.server.support.HttpHandlerFactory;
import pl.biltech.httpshare.util.MimeUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static pl.biltech.httpshare.httpd.NanoHTTPD.newFixedLengthResponse;
import static pl.biltech.httpshare.util.MimeUtil.classifyMimeAfterFileName;

public class NanoHttpHandlerFactory implements HttpHandlerFactory<Response> {
    public static final String TEXT_HTML = "text/html";
    public static final String APPLICATION_JSON = "application/json";
    private final EventPublisher eventPublisher;

    public NanoHttpHandlerFactory(EventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Response createRedirectHttpHandler(String redirectUrl) {
        Response r = newFixedLengthResponse(ResponseStatus.REDIRECT, MimeUtil.TEXT_HTML, "");
        r.addHeader("Location", redirectUrl);
        return r;
    }

    @Override
    public Response createDownloadHttpHandler(File file) {
        return createDownloadHttpHandler(file, MimeUtil.APPLICATION_OCTET_STREAM);
    }

    @Override
    public Response createDownloadHttpHandler(File file, String mime) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException ex) {
        }
        return newFixedLengthResponse(ResponseStatus.OK, mime, fis, file.length());
    }

    @Override
    public Response createErrorHttpHandler(Exception e) {
        return createErrorHttpHandler(ExceptionUtils.getStackTrace(e));
    }

    @Override
    public Response createErrorHttpHandler(String message) {
        return newFixedLengthResponse(ResponseStatus.BAD_REQUEST, TEXT_HTML, message);
    }

    @Override
    public Response createJsonHttpHandler(String json) {
        return newFixedLengthResponse(ResponseStatus.OK, APPLICATION_JSON, json);
    }

    @Override
    public Response createJsonHttpHandler(Object object) throws Exception {
        ObjectMapper om = new ObjectMapper();
        String json = om.writeValueAsString(new FileItem()
                .withPersistentDownload(true)
                .withRemovable(false)
                .withUrl("someUrl"));
        return createJsonHttpHandler(json);
    }

    @Override
    public Response createFolderContentHttpHandler(String folder, String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        String mime = classifyMimeAfterFileName(fileName);
        File file = new File(classLoader.getResource(folder + "/" + fileName).getFile());
        return createDownloadHttpHandler(file, mime);
    }
}
