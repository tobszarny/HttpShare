package pl.biltech.httpshare.httpd.socket.impl;

import pl.biltech.httpshare.httpd.socket.ServerSocketFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

public class IPServerSocketFactory implements ServerSocketFactory {
    private InetAddress inetAddress;
    private int port;

    public IPServerSocketFactory(int port, InetAddress inetAddress) {
        this.port = port;
        this.inetAddress = inetAddress;
    }

    @Override
    public ServerSocket create() throws IOException {
        return new ServerSocket(port, 10, inetAddress);

    }
}
