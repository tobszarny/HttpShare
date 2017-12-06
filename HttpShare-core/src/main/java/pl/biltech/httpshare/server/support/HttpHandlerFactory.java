package pl.biltech.httpshare.server.support;

import java.io.File;

@SuppressWarnings("restriction")
public interface HttpHandlerFactory<R> {

	String APPLICATION_OCTET_STREAM = "application/octet-stream";

	R createRedirectHttpHandler(String redirectUrl);

	R createDownloadHttpHandler(File file);

	R createDownloadHttpHandler(File file, String mime);

}
