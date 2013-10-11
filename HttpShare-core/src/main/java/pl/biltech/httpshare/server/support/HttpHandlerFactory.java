package pl.biltech.httpshare.server.support;

import java.io.File;

import com.sun.net.httpserver.HttpHandler;

@SuppressWarnings("restriction")
public interface HttpHandlerFactory {

	HttpHandler createRedirectHttpHandler(String redirectUrl);

	HttpHandler createDownloadHttpHandler(File file);

}
