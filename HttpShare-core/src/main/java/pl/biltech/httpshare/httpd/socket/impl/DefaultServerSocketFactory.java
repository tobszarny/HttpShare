package pl.biltech.httpshare.httpd.socket.impl;

import pl.biltech.httpshare.httpd.socket.ServerSocketFactory;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Creates a normal ServerSocket for TCP connections
 */
public class DefaultServerSocketFactory implements ServerSocketFactory {

    @Override
    public ServerSocket create() throws IOException {
        return new ServerSocket();
    }

}
