package org.example.transport.client.netty;

import io.netty.channel.Channel;
import org.example.transport.client.UnprocessedRequests;
import org.example.transport.client.RpcClient;
import org.example.transport.entity.ResponseFuture;
import org.example.transport.entity.RpcRequest;
import org.example.transport.entity.RpcResponse;
import org.example.factory.SingletonFactory;
import org.example.register.ServiceDiscovery;
import org.example.register.ZkServiceDiscovery;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

public class NettyRpcClient implements RpcClient {

//    private static final Bootstrap bootstrap;
//    private static final EventLoopGroup eventLoopGroup;
    private final ServiceDiscovery serviceDiscovery;
    private final UnprocessedRequests unprocessedRequests;


    public NettyRpcClient() {
        unprocessedRequests = SingletonFactory.getSingleton(UnprocessedRequests.class);
        this.serviceDiscovery = new ZkServiceDiscovery();
    }

//    static {
//        eventLoopGroup = new NioEventLoopGroup();
//        bootstrap = new Bootstrap();
//        bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)
//                .handler(new NettyClientInitializer())
//                //连接的超时时间，超过这个时间还是建立不上的话则代表连接失败
//                .option(CONNECT_TIMEOUT_MILLIS, 5000)
//                .option(ChannelOption.SO_KEEPALIVE, true)
//                //开启nagle算法
//                .option(ChannelOption.TCP_NODELAY, true);
//    }

    @Override
    public ResponseFuture sendRequest(RpcRequest request) {
        try {
            InetSocketAddress address = serviceDiscovery.discovery(request.getInterfaceName());

            Channel channel = ChannelProvider.get(address);
            if (channel == null) return null;
            ResponseFuture responseFuture = new ResponseFuture(new CompletableFuture<RpcResponse>());
            unprocessedRequests.put(request.getRequestId(), responseFuture);
            channel.writeAndFlush(request);
            //channel.closeFuture().sync();
            //System.out.println("在线程中" + channel.eventLoop().inEventLoop());
            return responseFuture;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
