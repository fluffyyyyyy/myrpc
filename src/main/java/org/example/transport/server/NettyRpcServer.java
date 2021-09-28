package org.example.transport.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.example.annotation.Service;
import org.example.annotation.ServiceScan;
import org.example.hook.CustomShutdownHook;
import org.example.util.ReflectUtil;

import java.util.Set;


public class NettyRpcServer implements RpcServer{
    private RpcExploreService rpcExploreService;

    public NettyRpcServer(RpcExploreService rpcExploreService) {
        this.rpcExploreService = rpcExploreService;
        scanService();
    }

    @Override
    public void start(int port) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        System.out.println("服务器启动了");
        CustomShutdownHook.getCustomShutdownHook().clearAll();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new NettyServerInitializer(rpcExploreService));
            ChannelFuture channelFuture = b.bind(port).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    @Override
    public void stop() {

    }

    public void scanService() {

        String mainClassName = ReflectUtil.getStackTrace();
        Class<?> startClass;
        try {
            startClass = Class.forName(mainClassName);
            if (!startClass.isAnnotationPresent(ServiceScan.class)) {
                throw new RuntimeException("不是扫描包");
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("类没找到");
        }
        String basePackage = "org.example.service";
        Set<Class<?>> classSet = ReflectUtil.getClasses(basePackage);
        for (Class<?> clazz : classSet) {
            if (clazz.isAnnotationPresent(Service.class)) {
                Object obj;
                try {
                    obj = clazz.newInstance();
                    rpcExploreService.explore(obj);
                } catch (InstantiationException | IllegalAccessException e) {
                    continue;
                }
            }
        }
    }
}
