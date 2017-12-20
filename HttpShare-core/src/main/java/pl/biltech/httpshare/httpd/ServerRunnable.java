package pl.biltech.httpshare.httpd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * The runnable that will be used for the main listening thread.
 */
public class ServerRunnable implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(ServerRunnable.class);

    private NanoHTTPD nanoHTTPD;
    private final int timeout;

    private IOException bindException;

    private boolean hasBinded = false;

    ServerRunnable(NanoHTTPD nanoHTTPD, int timeout) {
        this.nanoHTTPD = nanoHTTPD;
        this.timeout = timeout;
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = nanoHTTPD.getServerSocketFactory().create();
            serverSocket.setReuseAddress(true);
            nanoHTTPD.setMyServerSocket(serverSocket);
            hasBinded = true;
        } catch (IOException e) {
            this.bindException = e;
            return;
        }
        do {
            try {
                final Socket finalAccept = nanoHTTPD.getMyServerSocket().accept();
                if (this.timeout > 0) {
                    finalAccept.setSoTimeout(this.timeout);
                }
                final InputStream inputStream = finalAccept.getInputStream();
                nanoHTTPD.asyncRunner.exec(nanoHTTPD.createClientHandler(finalAccept, inputStream));
            } catch (IOException e) {
                LOG.debug("Communication with the client broken", e);
            }
        } while (!nanoHTTPD.getMyServerSocket().isClosed());
    }

    public boolean isBinded() {
        return hasBinded;
    }

    public IOException getBindException() {
        return bindException;
    }
}
