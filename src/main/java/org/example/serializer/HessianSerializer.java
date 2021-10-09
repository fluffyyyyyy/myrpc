package org.example.serializer;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import org.example.transport.entity.RpcRequest;
import org.example.transport.entity.RpcResponse;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class HessianSerializer implements Serializer{

    @Override
    public byte[] serialize(Object obj) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Hessian2Output out = new Hessian2Output(bos);
        try {
            out.writeObject(obj);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bos.toByteArray();
    }

    @Override
    public Object deserialize(byte[] bytes, int type) {
        Class<?> clazz = null;
        if (type == 0) clazz = RpcRequest.class;
        else if (type == 1) clazz = RpcResponse.class;
        Hessian2Input input = new Hessian2Input(new ByteArrayInputStream(bytes));
        try {
            return input.readObject(clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getType() {
        return 3;
    }
}
