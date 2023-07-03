package com.javayh.lite.swift.mesh;

import com.javayh.lite.swift.mesh.annotation.RpcService;

@RpcService(version = "1.0")
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String name) {
        String s = "Hello, " + name + "!";
        System.out.println(s);
        return s;
    }
}