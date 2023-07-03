package com.javayh.lite.swift.mesh;

import lombok.Data;

import java.io.Serializable;

/**
 * @author haiji
 */
@Data
public class RpcRequest implements Serializable {
    private String requestId;
    private String methodName;

    private String className;

    private Object[] parameters;

    private Class<?>[] parameterTypes;

    private String version;

}