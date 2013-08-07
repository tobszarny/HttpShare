package pl.biltech.httpshare.view.util;

import static pl.biltech.httpshare.util.Assert.isTrue;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.biltech.httpshare.annotation.VisibleForTesting;

/**
 * @author tomek, bilu
 * 
 */
public class NetworkUtil {

	private static final Logger logger = LoggerFactory.getLogger(NetworkUtil.class);
	private ServerSocket socket;

	// FIXME: any reason this method should be static while others are not?
	public static String getLocalHostName() throws UnknownHostException {
		return java.net.InetAddress.getLocalHost().getHostName();
	}

	public int findFirstFreePort(int startFrom) {
		isTrue(startFrom > 0);
		isTrue(startFrom < 65535);

		logger.debug("findFirstFreePort called for: {}", startFrom);
		try {
			socket = getServerSocket(startFrom);
			logger.info("Found free port: {}", startFrom);
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
				logger.error("Error during socket closing", e);
			}
		}
	}

	@VisibleForTesting
	ServerSocket getServerSocket(int startFrom) throws IOException {
		return new ServerSocket(startFrom);
	}
}
