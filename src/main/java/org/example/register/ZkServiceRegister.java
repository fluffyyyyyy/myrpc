package org.example.register;

import org.apache.curator.framework.CuratorFramework;
import org.example.register.util.CuratorUtils;

import java.net.InetSocketAddress;

public class ZkServiceRegister implements ServiceRegister{

    @Override
    public void register(String serviceName, InetSocketAddress serverAddress) {
        CuratorFramework zkClient = CuratorUtils.getZkClient();
        CuratorUtils.createNode(zkClient, serviceName, serverAddress);
    }
}
