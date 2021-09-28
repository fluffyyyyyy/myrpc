package org.example.transport.client.netty;

import lombok.Data;
import org.example.transport.client.RpcClient;
import org.example.transport.entity.ResponseFuture;
import org.example.transport.entity.RpcRequest;
import org.example.transport.entity.RpcResponse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class RpcUsedService {
    private Map<String, Object> proxyObjectMap;
    private Map<String, Class> classMap;
    private RpcClient rpcClient;

    public RpcUsedService(RpcClient client) {
        proxyObjectMap = new HashMap<>();
        classMap = new HashMap<>();
        this.rpcClient = client;
    }

    public void register(Class clazz) {
        if (!clazz.isInterface()) {
            throw new RuntimeException("暂时只支持接口类型的");
        }

        String className = clazz.getName();
        classMap.put(className, clazz);
        RpcInvocationHandler handler = new RpcInvocationHandler();
        handler.setClazz(clazz);
        Object proxyInstance = Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, handler);
        proxyObjectMap.put(className, proxyInstance);
    }

    public <T> T get(Class<T> clazz) {
        String className = clazz.getName();
        return (T)proxyObjectMap.get(className);
    }

    @Data
    class RpcInvocationHandler implements InvocationHandler {
        private Class clazz;

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            RpcRequest request = RpcRequest.builder().interfaceName(clazz.getName())
                    .methodName(method.getName())
                    .params(args)
                    .paramTypes(method.getParameterTypes())
                    .requestId(UUID.randomUUID().toString())
                    .build();
            ResponseFuture future = rpcClient.sendRequest(request);
            return future.get().getData();
        }
    }
}
