package pl.biltech.httpshare.httpd;

public class InetAddressMeta {
    private String ip;
    private String hostname;

    public InetAddressMeta(String ip, String hostname) {
        this.ip = ip;
        this.hostname = hostname;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }
}
