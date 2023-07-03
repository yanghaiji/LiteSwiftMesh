package com.javayh.lite.swift.mesh;

import lombok.Data;

import java.io.Serializable;

/**
 * @author haiji
 */
@Data
public class RpcResponse implements Serializable {

    private String requestId;
    private Object result;
    private Throwable error;

}