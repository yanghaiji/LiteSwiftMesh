package com.javayh.rpc;

import com.javayh.lite.swift.mesh.HelloService;
import com.javayh.lite.swift.mesh.RpcRequest;
import com.javayh.lite.swift.mesh.RpcResponse;
import com.javayh.lite.swift.mesh.discovery.zookeeper.RpcClient;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.UUID;

/**
 * <p>
 *
 * </p>
 *
 * @author hai ji
 * @version 1.0.0
 * @since 2023-06-30
 */
@SpringBootApplication(scanBasePackages = {"com.javayh.rpc","com.javayh.lite.swift.mesh"})
public class ClientApp {
    public static void main(String[] args) throws Exception {

        // 创建CuratorFramework实例
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient("localhost:2181", retryPolicy);
        curatorFramework.start();
        // 发送RPC请求
        RpcClient rpcClient = new RpcClient("localhost:8888", curatorFramework);
        RpcResponse rpcResponse = (RpcResponse) rpcClient.sendRequest(createRequest(HelloService.class.getName(), "1.0", "sayHello", new Class[]{String.class}, new Object[]{"Alice"}));
        System.out.println("-------------------");
        System.out.println(rpcResponse.getResult());
        // 关闭资源
        curatorFramework.close();
        rpcClient.close();
    }


    private static RpcRequest createRequest(String className, String version, String methodName, Class<?>[] parameterTypes, Object[] parameters) {
        RpcRequest request = new RpcRequest();
        request.setRequestId(UUID.randomUUID().toString());
        request.setClassName(className);
        request.setVersion(version);
        request.setMethodName(methodName);
        request.setParameterTypes(parameterTypes);
        request.setParameters(parameters);
        return request;
    }
}
