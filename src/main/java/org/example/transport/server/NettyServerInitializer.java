package org.example.transport.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import lombok.AllArgsConstructor;
import org.example.serializer.JsonSerializer;
import org.example.codec.MyDecoder;
import org.example.codec.MyEncoder;
import org.example.transport.server.handler.NettyRpcServerHandler;

@AllArgsConstructor
public class NettyServerInitializer extends ChannelInitializer<SocketChannel> {
    private RpcExploreService rpcExploreService;

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast(new MyEncoder(new JsonSerializer()));
        pipeline.addLast(new MyDecoder());


        pipeline.addLast(new NettyRpcServerHandler(rpcExploreService));
    }
}
