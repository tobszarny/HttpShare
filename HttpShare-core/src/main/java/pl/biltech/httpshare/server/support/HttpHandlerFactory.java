package pl.biltech.httpshare.server.support;

import java.io.File;

@SuppressWarnings("restriction")
public interface HttpHandlerFactory<R> {

	R createRedirectHttpHandler(String redirectUrl);

	R createDownloadHttpHandler(File file);

	R createDownloadHttpHandler(File file, String mime);

	R createErrorHttpHandler(Exception e);

	R createErrorHttpHandler(String message);

	R createJsonHttpHandler(String json) throws Exception;

	R createJsonHttpHandler(Object object) throws Exception;

    R createResourceFolderContentHttpHandler(String folder, String fileName);

    R createFolderContentHttpHandler(String folderPath, String fileName);

    R createFileDownloadHttpHandler(File file);
}
