package pl.biltech.httpshare.view.util;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author tomek
 * 
 */
public class NetworkUtil {

	private static final Logger logger = LoggerFactory
			.getLogger(NetworkUtil.class);

	public static int findFirstFreePort(int startFrom) {
		logger.debug("findFirstFreePort called");
		ServerSocket socket = null;
		try {
			socket = new ServerSocket(startFrom);
			logger.info("Found free port " + startFrom);
			return startFrom;
		} catch (IOException e) {

		} finally {
			if (socket != null)
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return findFirstFreePort(startFrom + 1);
	}

	public static String getLocalHostName() throws UnknownHostException {
		return java.net.InetAddress.getLocalHost().getHostName();
	}

}
