package com.javayh.lite.swift.mesh.registry.zookeeper;

import com.javayh.lite.swift.mesh.constant.Constant;
import org.apache.curator.framework.CuratorFramework;

/**
 * <p>
 * 用于将提供的服务注册到ZooKeeper中
 * </p>
 *
 * @author haiji
 */
public class ServiceRegistry {
    private final CuratorFramework curatorFramework;

    public ServiceRegistry(CuratorFramework curatorFramework) {
        this.curatorFramework = curatorFramework;
    }

    public void register(String serviceAddress) throws Exception {
        String servicePath = Constant.REGISTRY_PATH + "/" + serviceAddress;
        if (curatorFramework.checkExists().forPath(servicePath) == null) {
            curatorFramework.create().creatingParentsIfNeeded().forPath(servicePath);
            System.out.println("Service registered with ZooKeeper: " + servicePath);
        }
    }
}