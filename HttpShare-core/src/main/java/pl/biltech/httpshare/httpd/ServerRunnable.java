package pl.biltech.httpshare.httpd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * The runnable that will be used for the main listening thread.
 */
public class ServerRunnable implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(ServerRunnable.class);
    private final InetAddressMetaRepository inetAddressMetaRepository;

    private NanoHTTPD nanoHTTPD;
    private final int timeout;

    private IOException bindException;

    private boolean hasBinded = false;

    ServerRunnable(NanoHTTPD nanoHTTPD, int timeout, InetAddressMetaRepository inetAddressMetaRepository) {
        this.nanoHTTPD = nanoHTTPD;
        this.timeout = timeout;
        this.inetAddressMetaRepository = inetAddressMetaRepository;
    }

    @Override
    public void run() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = nanoHTTPD.getServerSocketFactory().create();
            serverSocket.setReuseAddress(true);
            nanoHTTPD.setMyServerSocket(serverSocket);
            hasBinded = true;
        } catch (IOException e) {
            this.bindException = e;
            return;
        }

        do {
            try {
                final Socket finalAcceptedSocket = nanoHTTPD.acceptServerSocketConnection();
                InetAddressMeta inetAddressMeta = null;
                if (inetAddressMetaRepository.contains(finalAcceptedSocket.getInetAddress().getHostAddress())) {
                    inetAddressMeta = inetAddressMetaRepository.get(finalAcceptedSocket.getInetAddress().getHostAddress());
                } else {
                    inetAddressMeta = inetAddressMetaRepository.addFromInetAddress(finalAcceptedSocket.getInetAddress());
                }

                nanoHTTPD.setInetAddressMeta(inetAddressMeta);

                if (this.timeout > 0) {
                    finalAcceptedSocket.setSoTimeout(this.timeout);
                }
                nanoHTTPD.asynchExec();
            } catch (IOException e) {
                LOG.debug("Communication with the client broken", e);
            }
        } while (!serverSocket.isClosed());
    }

    public boolean isBinded() {
        return hasBinded;
    }

    public IOException getBindException() {
        return bindException;
    }
}
