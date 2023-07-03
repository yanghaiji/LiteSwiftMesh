package com.javayh.rpc;

import com.javayh.lite.swift.mesh.registry.zookeeper.RpcServer;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
public class RpcServerApp {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(RpcServerApp.class, args);

        // 创建CuratorFramework实例
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient("localhost:2181", retryPolicy);
        curatorFramework.start();

        // 启动RPC服务器
        RpcServer rpcServer = new RpcServer("localhost:8888", curatorFramework);
        rpcServer.start();
        System.out.println("-------------------");
    }
}
