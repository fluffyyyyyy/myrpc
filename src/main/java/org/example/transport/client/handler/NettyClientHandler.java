package org.example.transport.client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.example.transport.client.UnprocessedRequests;
import org.example.transport.entity.RpcResponse;
import org.example.factory.SingletonFactory;

import java.util.Date;

public class NettyClientHandler extends SimpleChannelInboundHandler<RpcResponse> {
    private final UnprocessedRequests unprocessedRequests;

    public NettyClientHandler() {
        this.unprocessedRequests = SingletonFactory.getSingleton(UnprocessedRequests.class);
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, RpcResponse msg) throws Exception {
        System.out.println("NettyHandler接收到数据");
        unprocessedRequests.complete(msg);

        //ctx.channel().close();
        //ctx.channel().parent().close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("关闭连接时："+new Date());
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.WRITER_IDLE) {
                System.out.println("发送心跳包 [{}]" + ctx.channel().remoteAddress());
//                Channel channel = ChannelProvider.get((InetSocketAddress) ctx.channel().remoteAddress(), CommonSerializer.getByCode(CommonSerializer.DEFAULT_SERIALIZER));
//                RpcRequest rpcRequest = new RpcRequest();
//                rpcRequest.setHeartBeat(true);
//                channel.writeAndFlush(rpcRequest).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
