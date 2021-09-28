package org.example.extension;

//用于单个变量既可以做锁又可以做值
public class Holder <T>{
    private T value;

    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
    }
}
