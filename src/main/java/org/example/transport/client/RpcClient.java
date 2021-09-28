package org.example.transport.client;

import org.example.transport.entity.ResponseFuture;
import org.example.transport.entity.RpcRequest;

import java.util.concurrent.CompletableFuture;

public interface RpcClient {
    ResponseFuture sendRequest(RpcRequest request);
}
