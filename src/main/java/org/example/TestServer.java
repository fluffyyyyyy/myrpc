package org.example;

import org.example.annotation.ServiceScan;
import org.example.transport.server.NettyRpcServer;
import org.example.transport.server.RpcServer;
import org.example.transport.server.RpcExploreService;

@ServiceScan
public class TestServer {
    public static void main(String[] args) {
        RpcExploreService rpcExploreService = new RpcExploreService("127.0.0.1", 8899);
        RpcServer server = new NettyRpcServer(rpcExploreService);
        server.start(8899);
    }

}
