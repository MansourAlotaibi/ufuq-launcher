package io.github.mansouralotaibi.ufuq;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/** Enables TLS 1.2 on KitKat while delegating trust and hostname verification to Android. */
final class Tls12SocketFactory extends SSLSocketFactory {
    private final SSLSocketFactory delegate;
    Tls12SocketFactory(SSLSocketFactory delegate) { this.delegate = delegate; }
    private Socket enabled(Socket socket) {
        if (socket instanceof SSLSocket) ((SSLSocket) socket).setEnabledProtocols(new String[]{"TLSv1.2"});
        return socket;
    }
    @Override public String[] getDefaultCipherSuites() { return delegate.getDefaultCipherSuites(); }
    @Override public String[] getSupportedCipherSuites() { return delegate.getSupportedCipherSuites(); }
    @Override public Socket createSocket(Socket s, String h, int p, boolean c) throws IOException { return enabled(delegate.createSocket(s,h,p,c)); }
    @Override public Socket createSocket(String h, int p) throws IOException { return enabled(delegate.createSocket(h,p)); }
    @Override public Socket createSocket(String h, int p, InetAddress l, int lp) throws IOException { return enabled(delegate.createSocket(h,p,l,lp)); }
    @Override public Socket createSocket(InetAddress h, int p) throws IOException { return enabled(delegate.createSocket(h,p)); }
    @Override public Socket createSocket(InetAddress h, int p, InetAddress l, int lp) throws IOException { return enabled(delegate.createSocket(h,p,l,lp)); }
}
