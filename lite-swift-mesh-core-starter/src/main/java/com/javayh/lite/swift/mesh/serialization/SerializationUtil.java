package com.javayh.lite.swift.mesh.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * 序列化工具类
 *
 * @author haiji
 */
public class SerializationUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> byte[] serialize(T object) throws JsonProcessingException {
        return objectMapper.writeValueAsBytes(object);
    }

    public static <T> T deserialize(byte[] bytes, Class<T> clazz) throws IOException {
        return objectMapper.readValue(bytes, clazz);
    }


}