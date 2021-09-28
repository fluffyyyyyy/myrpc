package org.example.transport.client.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static io.netty.channel.ChannelOption.CONNECT_TIMEOUT_MILLIS;

public class ChannelProvider {
    private static EventLoopGroup eventLoopGroup;
    private static Bootstrap bootstrap;
    private static Map<String, Channel> channels;

    static {
        channels = new ConcurrentHashMap<>();
        eventLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)
                .handler(new NettyClientInitializer())
                //连接的超时时间，超过这个时间还是建立不上的话则代表连接失败
                .option(CONNECT_TIMEOUT_MILLIS, 5000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                //开启nagle算法
                .option(ChannelOption.TCP_NODELAY, true);
    }

    public static Channel get(InetSocketAddress inetSocketAddress) {
        String key = inetSocketAddress.getHostName() + inetSocketAddress.getPort();
        if (channels.containsKey(key)) {
            Channel channel = channels.get(key);
            if(channels != null && channel.isActive()) {
                return channel;
            } else {
                channels.remove(key);
            }
        }
        Channel channel = null;
        try {
            channel = bootstrap.connect(inetSocketAddress).sync().channel();
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("连接客户端时产生错误");
        }
        channels.put(key, channel);
        return channel;
    }
}
