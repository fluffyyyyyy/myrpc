package org.example.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.example.serializer.Serializer;

import java.util.List;

public class MyDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        short messageType = in.readShort();
        if (messageType != MessageType.REQUEST.getCode()
                && messageType != MessageType.RESPONSE.getCode()) {
            System.out.println("暂不支持这种数据");
            return;
        }
        System.out.println("MyDecoder接收到数据");
        short serializeType = in.readShort();
        Serializer serializer = Serializer.getSerializerByCode(serializeType);

        if (serializer == null) throw new RuntimeException("不存在对应序列化器");

        int len = in.readInt();
        byte[] bytes = new byte[len];
        in.readBytes(bytes);
        Object result = serializer.deserialize(bytes, messageType);
        out.add(result);
    }
}
