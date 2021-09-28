package org.example.transport.client.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.example.transport.client.handler.NettyClientHandler;
import org.example.serializer.KryoSerizlizer;
import org.example.codec.MyDecoder;
import org.example.codec.MyEncoder;

import java.util.concurrent.TimeUnit;

public class NettyClientInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast(new IdleStateHandler(0, 5, 0, TimeUnit.SECONDS));
        pipeline.addLast(new MyEncoder(new KryoSerizlizer()));
        pipeline.addLast(new MyDecoder());

        pipeline.addLast(new NettyClientHandler());
    }
}
