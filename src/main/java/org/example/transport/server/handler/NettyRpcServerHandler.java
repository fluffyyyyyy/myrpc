package org.example.transport.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.AllArgsConstructor;
import org.example.transport.entity.RpcRequest;
import org.example.transport.entity.RpcResponse;
import org.example.transport.server.RpcExploreService;

import java.net.InetSocketAddress;

@AllArgsConstructor
public class NettyRpcServerHandler extends SimpleChannelInboundHandler<RpcRequest> {
    private RpcExploreService rpcExploreService;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest msg) throws Exception {
        RpcResponse response = rpcExploreService.invoke(msg);
        InetSocketAddress insocket = (InetSocketAddress)ctx.channel().remoteAddress();
        System.out.println(insocket.getAddress().getHostAddress());
        System.out.println(insocket.getPort());
        System.out.println("在线程中" + ctx.executor().inEventLoop());
        ctx.writeAndFlush(response);
        //ctx.close();

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
