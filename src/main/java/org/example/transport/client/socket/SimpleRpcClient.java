package org.example.transport.client.socket;

import lombok.AllArgsConstructor;
import org.example.transport.entity.RpcRequest;
import org.example.transport.entity.RpcResponse;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

@AllArgsConstructor
public class SimpleRpcClient{
        private String host;
        private int port;


    public RpcResponse sendRequest(RpcRequest request) {
        try {
            Socket socket = new Socket(host, port);
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            System.out.println(socket.getInputStream().getClass().getName());
            System.out.println(request);
            oos.writeObject(request);
            oos.flush();
            RpcResponse response = (RpcResponse) ois.readObject();
            return response;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
