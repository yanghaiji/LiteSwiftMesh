package com.javayh.lite.swift.mesh.handler;

import com.javayh.lite.swift.mesh.HelloService;
import com.javayh.lite.swift.mesh.HelloServiceImpl;
import com.javayh.lite.swift.mesh.RpcRequest;
import com.javayh.lite.swift.mesh.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 用于处理RPC请求并给出响应
 * </p>
 *
 * @author haiji
 */
@Slf4j
public class RpcServerHandler extends SimpleChannelInboundHandler<RpcRequest> {
    private final Map<String, Object> handlerMap;

    public RpcServerHandler() {
        this.handlerMap = new HashMap<>();
    }

    @Override
    public void channelRead0(final ChannelHandlerContext ctx, RpcRequest request) {
        // 根据请求的接口名称和版本号获取对应的服务实现类
        String serviceName = request.getClassName();
        String version = request.getVersion();
        String serviceKey = serviceName + "." + version;
        Object serviceBean = handlerMap.get(serviceKey);
        if (serviceBean == null) {
            throw new RuntimeException("Service not found: " + serviceKey);
        }

        // 使用Java反射调用服务
        Class<?> serviceClass = serviceBean.getClass();
        String methodName = request.getMethodName();
        Class<?>[] parameterTypes = request.getParameterTypes();
        Object[] parameters = request.getParameters();

        Method method = null;
        try {
            method = serviceClass.getMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException e) {
            log.error("Method not found: {}", e.getMessage(), e);
            throw new RuntimeException("Method not found: " + methodName, e);
        }

        method.setAccessible(true);
        Object result;
        try {
            result = method.invoke(serviceBean, parameters);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new RuntimeException("Failed to invoke method: " + methodName, e);
        }

        // 返回响应结果
        RpcResponse response = new RpcResponse();
        response.setRequestId(request.getRequestId());
        response.setResult(result);
        ctx.writeAndFlush(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }

    public void addService(String serviceName, String version, Object serviceBean) {
        String serviceKey = serviceName + "." + version;
        handlerMap.put(serviceKey, serviceBean);
    }
}