package com.javayh.lite.swift.mesh.registry.zookeeper;

import com.javayh.lite.swift.mesh.HelloService;
import com.javayh.lite.swift.mesh.HelloServiceImpl;
import com.javayh.lite.swift.mesh.RpcRequest;
import com.javayh.lite.swift.mesh.RpcResponse;
import com.javayh.lite.swift.mesh.coder.RpcDecoder;
import com.javayh.lite.swift.mesh.coder.RpcEncoder;
import com.javayh.lite.swift.mesh.handler.RpcServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.curator.framework.CuratorFramework;

/**
 * 用于提供RPC服务。它使用Netty作为底层通信框架，并将服务注册到ZooKeeper中
 *
 * @author haiji
 */
public class RpcServer {
    private final String serverAddress;
    private final CuratorFramework curatorFramework;

    public RpcServer(String serverAddress, CuratorFramework curatorFramework) {
        this.serverAddress = serverAddress;
        this.curatorFramework = curatorFramework;
    }

    public void start() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) {
                            socketChannel.pipeline()
                                    // 解码请求
                                    .addLast(new RpcDecoder(RpcRequest.class))
                                    // 编码响应
                                    .addLast(new RpcEncoder(RpcResponse.class))
                                    // 处理请求
                                    .addLast(new RpcServerHandler());
                            // 添加服务到RpcServerHandler
                            RpcServerHandler rpcServerHandler = socketChannel.pipeline().get(RpcServerHandler.class);
                            rpcServerHandler.addService(HelloService.class.getName(), "1.0", new HelloServiceImpl());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            // 解析服务地址
            String[] addressArray = serverAddress.split(":");
            String ipAddress = addressArray[0];
            int port = Integer.parseInt(addressArray[1]);

            // 启动Netty服务器
            ChannelFuture future = serverBootstrap.bind(ipAddress, port).sync();
            System.out.println("RPC Server started on " + serverAddress);

            // 注册服务到ZooKeeper
            ServiceRegistry serviceRegistry = new ServiceRegistry(curatorFramework);
            serviceRegistry.register(serverAddress);

            // 等待服务器套接字关闭
            future.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}