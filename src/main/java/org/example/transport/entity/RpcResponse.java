package org.example.transport.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RpcResponse implements Serializable {
    private int code;
    private String message;
    private Object data;
    private Class<?> dataType;
    private String responseId;

    public static RpcResponse success(Object data, String id) {
        return RpcResponse.builder().code(200).data(data).responseId(id).dataType(data.getClass()).build();
    }

    public static RpcResponse fail() {
        return RpcResponse.builder().code(500).message("服务器发生错误").build();
    }
}
