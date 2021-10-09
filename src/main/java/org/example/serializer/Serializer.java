package org.example.serializer;

import org.example.extension.SPI;

@SPI("kryo")
public interface Serializer {
    byte[] serialize(Object obj);

    Object deserialize(byte[] bytes, int type);

    int getType();

    //这里其实不要这么写，可以存进一个HashMap，通过code来取得对象，避免每次生成
    static Serializer getSerializerByCode(int code) {
        switch (code) {
            case 0:
                return new ObjectSerializer();
            case 1:
                return new JsonSerializer();
            case 2:
                return new KryoSerializer();
            case 3:
                return new HessianSerializer();
            default:
                return null;
        }
    }
}
