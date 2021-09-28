package org.example.transport.server;

public interface RpcServer {
    public void start(int port);
    public void stop();
}
