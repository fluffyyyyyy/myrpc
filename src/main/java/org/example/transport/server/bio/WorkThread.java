package org.example.transport.server.bio;

import lombok.AllArgsConstructor;
import org.example.transport.entity.RpcRequest;
import org.example.transport.entity.RpcResponse;
import org.example.transport.server.RpcExploreService;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

@AllArgsConstructor
public class WorkThread implements Runnable{
    private Socket socket;
    private RpcExploreService rpcExploreService;

    @Override
    public void run() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            RpcRequest request = (RpcRequest) ois.readObject();
            RpcResponse response = getResponse(request);
            oos.writeObject(response);
            oos.flush();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("读取IO错误");
        }
    }

    public RpcResponse getResponse(RpcRequest request) {
        String interfaceName = request.getInterfaceName();
        Object service = rpcExploreService.getService(interfaceName);
        try {
            Method method = service.getClass().getMethod(request.getMethodName(), request.getParamTypes());
            Object invoke = method.invoke(service, request.getParams());
            return RpcResponse.success(invoke,"2q3");
            ///这里要改掉
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            System.out.println("方法执行错误");
            return RpcResponse.fail();
        }
    }
}
