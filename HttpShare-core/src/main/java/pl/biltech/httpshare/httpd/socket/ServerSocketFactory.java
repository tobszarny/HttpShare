package pl.biltech.httpshare.httpd.socket;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Factory to create ServerSocketFactories.
 */
public interface ServerSocketFactory {

    public ServerSocket create() throws IOException;

}
