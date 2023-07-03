//package com.javayh.lite.swift.mesh;
//
//import com.javayh.lite.swift.mesh.annotation.RpcService;
//import com.javayh.lite.swift.mesh.discovery.zookeeper.RpcClient;
//import com.javayh.lite.swift.mesh.registry.zookeeper.RpcServer;
//import org.apache.curator.RetryPolicy;
//import org.apache.curator.framework.CuratorFramework;
//import org.apache.curator.framework.CuratorFrameworkFactory;
//import org.apache.curator.retry.ExponentialBackoffRetry;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//
//import java.util.UUID;
//
//@SpringBootApplication
//public class RpcFrameworkApplication {
//
//
////    @RpcService(version = "1.0")
////    public static class HelloServiceImpl implements HelloService {
////        @Override
////        public String sayHello(String name) {
////            return "Hello, " + name + "!";
////        }
////    }
//
//    public static void main(String[] args) throws Exception {
//        SpringApplication.run(RpcFrameworkApplication.class, args);
//
//        // 创建CuratorFramework实例
//        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
//        CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient("localhost:2181", retryPolicy);
//        curatorFramework.start();
//
//        // 启动RPC服务器
//        RpcServer rpcServer = new RpcServer("localhost:8888", curatorFramework);
//        rpcServer.start();
//        System.out.println("-------------------");
//        // 发送RPC请求
//        RpcClient rpcClient = new RpcClient("localhost:8888", curatorFramework);
//        HelloService helloService = (HelloService) rpcClient.sendRequest(createRequest(HelloService.class.getName(), "1.0", "sayHello", new Class[]{String.class}, new Object[]{"Alice"}));
//        System.out.println(helloService.sayHello("Alice"));
//
//        // 关闭资源
//        curatorFramework.close();
//        rpcClient.close();
//    }
//
//
//
//    private static RpcRequest createRequest(String className, String version, String methodName, Class<?>[] parameterTypes, Object[] parameters) {
//        RpcRequest request = new RpcRequest();
//        request.setRequestId(UUID.randomUUID().toString());
//        request.setClassName(className);
//        request.setVersion(version);
//        request.setMethodName(methodName);
//        request.setParameterTypes(parameterTypes);
//        request.setParameters(parameters);
//        return request;
//    }
//}