package pl.biltech.httpshare.server.support.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.exception.ExceptionUtils;
import pl.biltech.httpshare.eventbus.publisher.EventPublisher;
import pl.biltech.httpshare.httpd.http.Response;
import pl.biltech.httpshare.httpd.http.ResponseStatus;
import pl.biltech.httpshare.repository.FileRepository;
import pl.biltech.httpshare.repository.model.FileItem;
import pl.biltech.httpshare.server.support.HttpHandlerFactory;
import pl.biltech.httpshare.util.MimeUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static pl.biltech.httpshare.httpd.NanoHTTPD.newFixedLengthResponse;
import static pl.biltech.httpshare.util.MimeUtil.classifyMimeAfterFileName;

public class NanoHttpHandlerFactory implements HttpHandlerFactory<Response> {
    public static final String TEXT_HTML = "text/html";
    public static final String APPLICATION_JSON = "application/json";
    private final EventPublisher eventPublisher;
    private FileRepository fileRepository;

    public NanoHttpHandlerFactory(EventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Response createRedirectHttpHandler(String redirectUrl) {
        Response redirect = newFixedLengthResponse(ResponseStatus.REDIRECT, MimeUtil.TEXT_HTML, "");
        redirect.addHeader("Location", redirectUrl);
        return redirect;
    }

    @Override
    public Response createDownloadHttpHandler(File file) throws Exception {
        return createDownloadHttpHandler(file, MimeUtil.APPLICATION_OCTET_STREAM);
    }

    @Override
    public Response createDownloadHttpHandler(File file, String mime) throws Exception {
        FileInputStream fis = null;
        fis = new FileInputStream(file);
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
        List<FileItem> list = new ArrayList<>();
        if (object == null) {
            return createJsonHttpHandler("{}");
        } else {
            if (List.class.isAssignableFrom(object.getClass())) {
                List objects = (List) object;
                for (Object o : objects) {
                    list.add((FileItem) o);
                }
            }
        }
        String json = om.writeValueAsString(list);
        return createJsonHttpHandler(json);
    }

    @Override
    public Response createResourceFolderContentHttpHandler(String folder, String fileName) throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(folder + "/" + fileName).getFile());
        return createFileDownloadHttpHandler(file);
    }

    @Override
    public Response createFolderContentHttpHandler(String folder, String fileName) throws Exception {
        File file = new File(folder + "/" + fileName);
        return createFileDownloadHttpHandler(file);
    }

    @Override
    public Response createFileDownloadHttpHandler(File file) throws Exception {
        if (file == null || !file.exists()) {
            throw new FileNotFoundException(file == null ? "null" : file.getAbsolutePath());
        }
        String mime = classifyMimeAfterFileName(file.getName());
        return createDownloadHttpHandler(file, mime);
    }

}
