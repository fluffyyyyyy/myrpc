package org.example.register;

import java.net.InetSocketAddress;

public interface ServiceDiscovery {
    InetSocketAddress discovery(String serviceName);
}
