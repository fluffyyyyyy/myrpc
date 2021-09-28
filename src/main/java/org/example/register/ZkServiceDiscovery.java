package org.example.register;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.example.loadbalance.LoadBalance;
import org.example.loadbalance.RoundLoadBalance;
import org.example.register.util.CuratorUtils;

import java.net.InetSocketAddress;
import java.util.List;

public class ZkServiceDiscovery implements ServiceDiscovery{

    private LoadBalance loadBalance;

    public ZkServiceDiscovery() {
        this.loadBalance = new RoundLoadBalance();
    }

    @Override
    public InetSocketAddress discovery(String serviceName) {
        CuratorFramework zkClient = CuratorUtils.getZkClient();
        List<String> list = CuratorUtils.getChildrenNodes(zkClient, serviceName);
        String address = loadBalance.balance(list);
        return parseAddress(address);
    }

    // 字符串解析为地址
    private InetSocketAddress parseAddress(String address) {
        String[] result = address.split(":");
        return new InetSocketAddress(result[0], Integer.parseInt(result[1]));
    }
}
