package org.example.hook;

import org.example.register.util.CuratorUtils;
import java.net.InetSocketAddress;

public class CustomShutdownHook {
    private static final CustomShutdownHook CUSTOM_SHUTDOWN_HOOK = new CustomShutdownHook();

    public static CustomShutdownHook getCustomShutdownHook() {
        return CUSTOM_SHUTDOWN_HOOK;
    }

    public void clearAll() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 8899);
                CuratorUtils.clearRegistry(CuratorUtils.getZkClient(), inetSocketAddress);
                Thread.sleep(5000);
                System.out.println("优雅关闭");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
    }
}
