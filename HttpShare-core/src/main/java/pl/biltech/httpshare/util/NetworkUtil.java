package pl.biltech.httpshare.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.biltech.httpshare.annotation.VisibleForTesting;

import java.io.Closeable;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;

import static pl.biltech.httpshare.util.Assert.assertTrue;

/**
 * @author tomek, bilu
 */
public class NetworkUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(NetworkUtil.class);
    private ServerSocket socket;

    public static final void safeClose(Object closeable) {
        try {
            if (closeable != null) {
                if (closeable instanceof Closeable) {
                    ((Closeable) closeable).close();
                } else if (closeable instanceof Socket) {
                    ((Socket) closeable).close();
                } else if (closeable instanceof ServerSocket) {
                    ((ServerSocket) closeable).close();
                } else {
                    throw new IllegalArgumentException("Unknown object to close");
                }
            }
        } catch (IOException e) {
            LOGGER.error("Could not close", e);
        }
    }

    public String getLocalHostName() throws UnknownHostException {
        String hostName = InetAddress.getLocalHost().getHostName();

        InetAddress ip = null;
        try {
            ip = InetAddress.getByName(hostName);
            LOGGER.debug("Resolved hostname {} to {}", hostName, ip);
        } catch (Exception e) {
            LOGGER.error("Could not resolve IP", e);
        }

        return hostName;
    }

    public int findFirstFreePort(int startFrom) {
        assertTrue(startFrom > 0);
        assertTrue(startFrom < 65535);

        LOGGER.debug("findFirstFreePort called for: {}", startFrom);
        try {
            socket = getServerSocket(startFrom);
            LOGGER.info("Found free port: {}", startFrom);
            return startFrom;
        } catch (IOException e) {
        } finally {
            closeSocket();
        }
        return findFirstFreePort(startFrom + 1);
    }

    private void closeSocket() {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                LOGGER.error("Error during socket closing", e);
            }
        }
    }

    @VisibleForTesting
    ServerSocket getServerSocket(int startFrom) throws IOException {
        return new ServerSocket(startFrom);
    }


    /**
     * Decode encoded URL <code>String</code> values.
     *
     * @param str the percent encoded <code>String</code>
     * @return expanded form of the input, for example "foo%20bar" becomes
     * "foo bar"
     */
    public static String decode(String str) {
        String decoded = null;
        try {
            decoded = URLDecoder.decode(str, "UTF8");
        } catch (UnsupportedEncodingException ignored) {
            LOGGER.warn("Encoding not supported, ignored", ignored);
        }
        return decoded;
    }
}
