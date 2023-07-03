package com.javayh.lite.swift.mesh.handler;

import com.javayh.lite.swift.mesh.RpcResponse;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author haiji
 */
@Slf4j
public class RpcClientHandler extends SimpleChannelInboundHandler<RpcResponse> {
    private static volatile RpcResponse response;

    public static RpcResponse getResponse() {
        return response;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse rpcResponse) {
        response = rpcResponse;
        Channel channel = ctx.channel();
        log.info("channelRead0 localAddress: {} , remoteAddress: {} ,rpcResponse : {} ", channel.localAddress(), channel.remoteAddress(), rpcResponse);
        ctx.close();
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        log.info("channelRegistered localAddress: {} , remoteAddress: {}  ", channel.localAddress(), channel.remoteAddress());
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        log.info("channelUnregistered localAddress: {} , remoteAddress: {}  ", channel.localAddress(), channel.remoteAddress());
        super.channelUnregistered(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        Channel channel = ctx.channel();
        log.info("localAddress: {} , remoteAddress: {} ,cause : {} ", channel.localAddress(), channel.remoteAddress(), cause);
        ctx.close();
    }
}