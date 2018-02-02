package pl.biltech.httpshare.server.support;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

@SuppressWarnings("restriction")
public interface HttpHandlerFactory<R> {

    R createRedirectHttpHandler(String redirectUrl);

    R createDownloadHttpHandler(File file) throws Exception;

    R createDownloadHttpHandler(File file, String mime) throws Exception;

    R createDownloadHttpHandler(InputStream stream, String mime);

    R createErrorHttpHandler(Exception e);

    R createErrorHttpHandler(String message);

    R createJsonHttpHandler(String json) throws Exception;

    R createJsonHttpHandler(Object object) throws Exception;

    R createResourceFolderContentHttpHandler(String folder, String fileName) throws Exception;

    R createFolderContentHttpHandler(String folderPath, String fileName) throws Exception;

    R createFileDownloadHttpHandler(File file) throws FileNotFoundException, Exception;

    R createFileStreamDownloadHttpHandler(InputStream inputStream, String mime);
}