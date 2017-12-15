package pl.biltech.httpshare.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.biltech.httpshare.annotation.VisibleForTesting;

import java.io.Closeable;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    public static String getLocalHostName() {
        String hostName = "unknown";
        try {
            hostName = InetAddress.getLocalHost().getHostName();
            InetAddress ip = InetAddress.getByName(hostName);

            LOGGER.debug("Resolved hostname {} to {}", hostName, ip);
        } catch (Exception e) {
            LOGGER.error("Could not resolve IP", e);
        }

        return hostName;
    }

    public static int findFirstFreePort(int startFrom) {
        assertTrue(startFrom > 0);
        assertTrue(startFrom < 65535);

        ServerSocket socket = null;
        LOGGER.debug("findFirstFreePort called for: {}", startFrom);
        try {
            socket = getServerSocket(startFrom);
            LOGGER.info("Found free port: {}", startFrom);
            return startFrom;
        } catch (IOException e) {
        } finally {
            closeSocket(socket);
        }
        return findFirstFreePort(startFrom + 1);
    }

    private static void closeSocket(ServerSocket socket) {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                LOGGER.error("Error during socket closing", e);
            }
        }
    }

    @VisibleForTesting
    public static ServerSocket getServerSocket(int port) throws IOException {
        return new ServerSocket(port);
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

    public static void printInetAdressesByHostName(InetAddress[] allByName) {
        String[] titles = new String[]{"INET", "isSiteLocalAddress", "isAnyLocalAddress", "isLoopbackAddress", "isLinkLocalAddress",
                "isMulticastAddress", "isMCGlobal", "isMCLinkLocal", "isMCNodeLocal", "isMCOrgLocal", "isMCSiteLocal"};

        int[] columnLengths = new int[titles.length];

        for (int i = 0; i < titles.length; i++) {
            String title = titles[i];
            Function<? super InetAddress, ? extends String> function = null;
            switch (i) {
                case 0:
                    function = new ToStringFunction();
                    break;
                default:
                    function = new ToGetterValue(title);
            }
            columnLengths[i] = Math.max(title.length(), Arrays.stream(allByName)
                    .map(function).mapToInt(t -> t.length()).max().orElse(0)
            );
        }

        List<String> collect = Arrays.stream(columnLengths).mapToObj(c -> "%" + Integer.toString(c) + "s").collect(Collectors.toList());
        String patter = String.join(" ", collect);

        System.out.println(String.format(patter, titles));

        for (int i = 0; i < allByName.length; i++) {
            InetAddress inetAddress = allByName[i];
            System.out.println(String.format(patter,
                    inetAddress,
                    inetAddress.isSiteLocalAddress(),
                    inetAddress.isAnyLocalAddress(),
                    inetAddress.isLoopbackAddress(),
                    inetAddress.isLinkLocalAddress(),
                    inetAddress.isMulticastAddress(),
                    inetAddress.isMCGlobal(),
                    inetAddress.isMCLinkLocal(),
                    inetAddress.isMCNodeLocal(),
                    inetAddress.isMCOrgLocal(),
                    inetAddress.isMCSiteLocal()
            ));
        }
    }

    private static class ToStringFunction implements Function<InetAddress, String> {

        @Override
        public String apply(InetAddress inetAddress) {
            return inetAddress.toString();
        }
    }

    private static class ToGetterValue implements Function<InetAddress, String> {

        String method;

        public ToGetterValue(String method) {
            this.method = method;
        }

        @Override
        public String apply(InetAddress inetAddress) {
            try {
                Method declaredMethod = InetAddress.class.getDeclaredMethod(method, new Class[]{});
                boolean value = (boolean) declaredMethod.invoke(inetAddress, new Object[]{});
                return Boolean.toString(value);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            return "dupa";
        }
    }
}
