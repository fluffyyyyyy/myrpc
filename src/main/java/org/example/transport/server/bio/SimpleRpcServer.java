package org.example.transport.server.bio;

import org.example.transport.server.RpcExploreService;
import org.example.transport.server.RpcServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleRpcServer implements RpcServer {
    private RpcExploreService rpcExploreService;

    public SimpleRpcServer(RpcExploreService rpcExploreService) {
        this.rpcExploreService = rpcExploreService;
    }

    @Override
    public void start(int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("服务端启动了");
            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(new WorkThread(socket, rpcExploreService)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("服务器启动失败");
        }
    }

    @Override
    public void stop() {

    }
}
