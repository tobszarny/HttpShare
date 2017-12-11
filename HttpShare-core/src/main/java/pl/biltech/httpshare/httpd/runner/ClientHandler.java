package pl.biltech.httpshare.httpd.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.biltech.httpshare.httpd.NanoHTTPD;
import pl.biltech.httpshare.httpd.http.HTTPSession;
import pl.biltech.httpshare.httpd.manager.file.TempFileManager;
import pl.biltech.httpshare.httpd.manager.file.TempFileManagerFactory;
import pl.biltech.httpshare.util.NetworkUtil;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

/**
 * The runnable that will be used for every new client connection.
 */
public class ClientHandler implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(ClientHandler.class);

    private final TempFileManagerFactory tempFileManagerFactory;
    private NanoHTTPD nanoHTTPD;
    private final InputStream inputStream;

    private final Socket acceptSocket;

    public ClientHandler(NanoHTTPD nanoHTTPD, InputStream inputStream, Socket acceptSocket, TempFileManagerFactory tempFileManagerFactory) {
        this.nanoHTTPD = nanoHTTPD;
        this.inputStream = inputStream;
        this.acceptSocket = acceptSocket;
        this.tempFileManagerFactory = tempFileManagerFactory;
    }

    public void close() {
        NetworkUtil.safeClose(this.inputStream);
        NetworkUtil.safeClose(this.acceptSocket);
    }

    @Override
    public void run() {
        OutputStream outputStream = null;
        try {
            outputStream = this.acceptSocket.getOutputStream();
            TempFileManager tempFileManager = tempFileManagerFactory.create();
            HTTPSession session = new HTTPSession(nanoHTTPD, tempFileManager, this.inputStream, outputStream, this.acceptSocket.getInetAddress());
            while (!this.acceptSocket.isClosed()) {
                session.execute();
            }
        } catch (Exception e) {
            // When the socket is closed by the client,
            // we throw our own SocketException
            // to break the "keep alive" loop above. If
            // the exception was anything other
            // than the expected SocketException OR a
            // SocketTimeoutException, print the
            // stacktrace
            if (!(e instanceof SocketException && "NanoHttpd Shutdown".equals(e.getMessage())) && !(e instanceof SocketTimeoutException)) {
                LOG.error("Communication with the client broken, or an bug in the handler code", e);
            }
        } finally {
            NetworkUtil.safeClose(outputStream);
            NetworkUtil.safeClose(this.inputStream);
            NetworkUtil.safeClose(this.acceptSocket);
            nanoHTTPD.getAsyncRunner().closed(this);
        }
    }
}
