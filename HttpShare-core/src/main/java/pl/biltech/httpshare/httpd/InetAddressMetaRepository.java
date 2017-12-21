package pl.biltech.httpshare.httpd;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

public class InetAddressMetaRepository {

    private final Map<String, InetAddressMeta> repo;

    public InetAddressMetaRepository() {
        repo = new HashMap<>();
    }

    public InetAddressMeta addFromInetAddress(InetAddress inetAddress) {
        String remoteIp = inetAddress.isLoopbackAddress() || inetAddress.isAnyLocalAddress() ? "127.0.0.1" : inetAddress.getHostAddress().toString();
        String remoteHostname = inetAddress.isLoopbackAddress() || inetAddress.isAnyLocalAddress() ? "localhost" : inetAddress.getHostName().toString();
        InetAddressMeta inetAddressMeta = new InetAddressMeta(remoteIp, remoteHostname);
        add(inetAddressMeta);
        return inetAddressMeta;
    }

    public void add(InetAddressMeta inetAddressMeta) {
        repo.put(inetAddressMeta.getIp(), inetAddressMeta);
    }

    public boolean contains(String hostAddress) {
        return repo.containsKey(hostAddress);
    }

    public InetAddressMeta get(String hostAddress) {
        return repo.get(hostAddress);
    }
}
