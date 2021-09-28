package org.example;

import org.example.transport.client.netty.NettyRpcClient;
import org.example.transport.client.RpcClient;
import org.example.transport.client.netty.RpcUsedService;
import org.example.service.UserService;

import java.util.Random;

public class TestClient {
    public static void main(String[] args) {
        RpcClient NettyRpcClient = new NettyRpcClient();

        RpcUsedService rpcUsedService = new RpcUsedService(NettyRpcClient);
        rpcUsedService.register(UserService.class);
        UserService service = rpcUsedService.get(UserService.class);
        Random random = new Random();
        System.out.println("main " + service.getUserByUserId(random.nextInt(50)));

    }
}
