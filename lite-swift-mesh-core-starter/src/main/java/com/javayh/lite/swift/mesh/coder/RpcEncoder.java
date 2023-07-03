package com.javayh.lite.swift.mesh.coder;

import com.javayh.lite.swift.mesh.serialization.SerializationUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.SneakyThrows;

/**
 * @author haiji
 */
public class RpcEncoder extends MessageToByteEncoder {
    private final Class<?> genericClass;

    public RpcEncoder(Class<?> genericClass) {
        this.genericClass = genericClass;
    }

    @SneakyThrows
    @Override
    protected void encode(ChannelHandlerContext ctx, Object object, ByteBuf out) {
        if (genericClass.isInstance(object)) {
            byte[] data = SerializationUtil.serialize(object);
            out.writeInt(data.length);
            out.writeBytes(data);
        }
    }
}