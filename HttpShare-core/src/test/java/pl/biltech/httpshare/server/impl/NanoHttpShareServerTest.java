package pl.biltech.httpshare.server.impl;

import org.junit.Test;
import pl.biltech.httpshare.util.NetworkUtil;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NanoHttpShareServerTest {

    @Test
    public void name() throws UnknownHostException {
        NetworkUtil networkUtil = new NetworkUtil();

        String hostname = networkUtil.getLocalHostName();
        InetAddress[] allByName = InetAddress.getAllByName(hostname);
        Integer maxStrLen = Arrays.stream(allByName).map(a -> a.toString().length()).max(Integer::compare).get();

        String[] headers = new String[]{"INET", "LocalAddress", "LinkLocalAddress", "LoopbackAddress", "MCGlobal", "MCLinkLocal", "MCNodeLocal", "MCSiteLocal", "MCOrgLocal", "MulticastAddress"};
        List<Integer> headersLength = new ArrayList<>();
        Arrays.stream(headers).forEach(h -> headersLength.add(h.length()));

        int i = 0;
        StringBuilder builder = new StringBuilder();
        builder.append(getFormatting(0, maxStrLen)).append("  ");
        for (int j = 1; j < headers.length; j++) {
            builder.append(getFormatting(j, Math.max(5, headersLength.get(j)))).append("  ");
        }

        String format = builder.toString();

//        System.out.println(format);

        System.out.println(String.format(format, headers));

        for (InetAddress inetAddress : allByName) {
            System.out.println(String.format(format,
                    inetAddress.toString(),
                    inetAddress.isAnyLocalAddress(),
                    inetAddress.isLinkLocalAddress(),
                    inetAddress.isLoopbackAddress(),
                    inetAddress.isMCGlobal(),
                    inetAddress.isMCLinkLocal(),
                    inetAddress.isMCNodeLocal(),
                    inetAddress.isMCSiteLocal(),
                    inetAddress.isMCOrgLocal(),
                    inetAddress.isMulticastAddress()
            ));
        }
    }

    private String getFormatting(int position, int maxStrLen) {
        return "%" + ++position + "$" + maxStrLen + "s";
    }
}