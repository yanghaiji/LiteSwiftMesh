package com.javayh.lite.swift.mesh.discovery.zookeeper;

import com.javayh.lite.swift.mesh.RpcRequest;
import com.javayh.lite.swift.mesh.RpcResponse;
import com.javayh.lite.swift.mesh.coder.RpcDecoder;
import com.javayh.lite.swift.mesh.coder.RpcEncoder;
import com.javayh.lite.swift.mesh.handler.RpcClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;

import javax.annotation.PreDestroy;

/**
 * @author haiji
 */
@Slf4j
public class RpcClient {
    private final String serverAddress;
    private final CuratorFramework curatorFramework;
    private final static Bootstrap bootstrap;
    private final static EventLoopGroup group;

    public RpcClient(String serverAddress, CuratorFramework curatorFramework) {
        this.serverAddress = serverAddress;
        this.curatorFramework = curatorFramework;
    }

    static {
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) {
                        socketChannel.pipeline()
                                .addLast(new RpcEncoder(RpcRequest.class)) // 编码请求
                                .addLast(new RpcDecoder(RpcResponse.class)) // 解码响应
                                .addLast(new RpcClientHandler()); // 处理响应
                    }
                })
                .option(ChannelOption.TCP_NODELAY, true);
    }

    public Object sendRequest(RpcRequest request) throws Exception {
        // 解析服务器地址
        String[] addressArray = serverAddress.split(":");
        String ipAddress = addressArray[0];
        int port = Integer.parseInt(addressArray[1]);

        // 创建连接
        ChannelFuture future = bootstrap.connect(ipAddress, port).sync();
        if (future.isSuccess()) {
            log.info("lite swift mesh starter 连接成功");
        }
        // 发送请求
        future.channel().writeAndFlush(request).sync();

        // 等待响应
        future.channel().closeFuture().sync();

        // 返回响应结果
        return RpcClientHandler.getResponse();
    }

    @PreDestroy
    public void close() {
        group.shutdownGracefully();
    }
}