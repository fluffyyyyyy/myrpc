package org.example.extension;

import cn.hutool.core.util.StrUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ExtensionLoader<T> {
    private final Map<String, T> extensionsCache = new ConcurrentHashMap<>();

    private static final Map<Class<?>, ExtensionLoader<?>> extensionLoaderCache = new ConcurrentHashMap<>();

    private final Holder<Map<String, Class<?>>> extensionClassesCache = new Holder<>();

    private final Map<String, Object> createExtensionLockMap = new ConcurrentHashMap<>();

    private final Class<T> type;

    private static final String EXTENSION_PATH = "META-INF/myrpc/";

    /**
     * 默认扩展名缓存
     */
    private final String defaultNameCache;

    private ExtensionLoader(Class<T> type) {
        this.type = type;
        SPI annotation = type.getAnnotation(SPI.class);
        defaultNameCache = annotation.value();
    }

    public static <S> ExtensionLoader<S> getLoader(Class<S> type) {
        // 扩展类型必须是接口
        if (!type.isInterface()) {
            throw new IllegalStateException(type.getName() + " is not interface");
        }
        SPI annotation = type.getAnnotation(SPI.class);
        if (annotation == null) {
            throw new IllegalStateException(type.getName() + " has not @SPI annotation.");
        }
        ExtensionLoader<?> extensionLoader = extensionLoaderCache.get(type);
        if (extensionLoader != null) {
            //noinspection unchecked
            return (ExtensionLoader<S>) extensionLoader;
        }
        extensionLoader = new ExtensionLoader<>(type);
        extensionLoaderCache.putIfAbsent(type, extensionLoader);
        //noinspection unchecked
        return (ExtensionLoader<S>) extensionLoader;
    }

    /**
     * 获取默认的扩展类实例，会自动加载 @SPI 注解中的 value 指定的类实例
     *
     * @return 返回该类的注解 @SPI.value 指定的类实例
     */
    public T getDefaultExtension() {
        return getExtension(defaultNameCache);
    }


    public T getExtension(String name) {
        if (StrUtil.isBlank(name)) {
            return getDefaultExtension();
        }
        // 从缓存中获取单例
        T extension = extensionsCache.get(name);
        if (extension == null) {
            Object lock = createExtensionLockMap.computeIfAbsent(name, k -> new Object());
            //noinspection SynchronizationOnLocalVariableOrMethodParameter
            synchronized (lock) {
                extension = extensionsCache.get(name);
                if (extension == null) {
                    extension = createExtension(name);
                    extensionsCache.put(name, extension);
                }
            }
        }
        return extension;
    }

    /**
     * 获取自适应扩展类
     *
     * @return 动态代理自适应类
     */// TODO: 2021/9/27  
//    public T getAdaptiveExtension() {
//    }

    private T createExtension(String name) {
        // 获取当前类型所有扩展类
        Map<String, Class<?>> extensionClasses = getAllExtensionClasses();
        // 再根据名字找到对应的扩展类
        Class<?> clazz = extensionClasses.get(name);
        if (clazz == null) {
            throw new IllegalStateException("Extension not found. name=" + name + ", type=" + type.getName());
        }
        try {
            //noinspection unchecked
            return (T) clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalStateException("Extension not found. name=" + name + ", type=" + type.getName() + ". " + e.toString());
        }
    }


    private Map<String, Class<?>> getAllExtensionClasses() {
        Map<String, Class<?>> extensionClasses = extensionClassesCache.get();
        if (extensionClasses != null) {
            return extensionClasses;
        }
        synchronized (extensionClassesCache) {
            extensionClasses = extensionClassesCache.get();
            if (extensionClasses == null) {
                extensionClasses = loadClassesFromResources();
                extensionClassesCache.set(extensionClasses);
            }
        }
        return extensionClasses;
    }


    private Map<String, Class<?>> loadClassesFromResources() {
        Map<String, Class<?>> extensionClasses = new ConcurrentHashMap<>();
        // 扩展配置文件名
        String fileName = EXTENSION_PATH + type.getName();
        // 拿到资源文件夹
        ClassLoader classLoader = ExtensionLoader.class.getClassLoader();
        try {
            Enumeration<URL> resources = classLoader.getResources(fileName);
            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8))) {
                    // 开始读文件
                    while (true) {
                        String line = reader.readLine();
                        if (line == null) {
                            break;
                        }
                        parseLine(line, extensionClasses);
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException("Parse file fail. " + e.toString());
        }
        return extensionClasses;
    }

    private void parseLine(String line, Map<String, Class<?>> extensionClasses) throws ClassNotFoundException {
        line = line.trim();
        // 忽略#号开头的注释
        if (line.startsWith("#")) {
            return;
        }
        String[] kv = line.split("=");
        if (kv.length != 2 || kv[0].length() == 0 || kv[1].length() == 0) {
            throw new IllegalStateException("Extension file parsing error. Invalid format!");
        }
        if (extensionClasses.containsKey(kv[0])) {
            throw new IllegalStateException(kv[0] + "已存在");
        }
        Class<?> clazz = ExtensionLoader.class.getClassLoader().loadClass(kv[1]);
        extensionClasses.put(kv[0], clazz);
    }
}
