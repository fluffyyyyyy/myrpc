package org.example.transport.server;

import org.example.transport.entity.RpcRequest;
import org.example.transport.entity.RpcResponse;
import org.example.register.ServiceRegister;
import org.example.register.ZkServiceRegister;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class RpcExploreService {
    private final Map<String, Object> serviceProvider;
    private final int port;
    private final String host;
    private final ServiceRegister serviceRegister;

    public RpcExploreService(String host, int port) {
        this.serviceProvider = new HashMap<>();
        this.host = host;
        this.port = port;
        this.serviceRegister = new ZkServiceRegister();
    }

    public void explore(Object service) {
        Class<?>[] interfaces = service.getClass().getInterfaces();
        for (Class c : interfaces) {
            serviceProvider.put(c.getName(), service);
            serviceRegister.register(c.getName(), new InetSocketAddress(host, port));
        }
    }

    public RpcResponse invoke(RpcRequest request) {
        String interfaceName = request.getInterfaceName();
        Object service = getService(interfaceName);
        try {
            Method method = service.getClass().getMethod(request.getMethodName(), request.getParamTypes());
            Object invoke = method.invoke(service, request.getParams());
            return RpcResponse.success(invoke, request.getRequestId());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            System.out.println("方法执行错误");
            return RpcResponse.fail();
        }
    }

    public Object getService(String serviceName) {
        return serviceProvider.get(serviceName);
    }
}
