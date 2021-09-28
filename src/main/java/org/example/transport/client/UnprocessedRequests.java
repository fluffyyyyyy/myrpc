package org.example.transport.client;

import org.example.transport.entity.ResponseFuture;
import org.example.transport.entity.RpcResponse;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class UnprocessedRequests {
    private static ConcurrentHashMap<String, ResponseFuture> unprocessedRequests;

    static {
        unprocessedRequests = new ConcurrentHashMap<>();
    }

    public void remove(String requestId) { unprocessedRequests.remove(requestId); }

    public void complete(RpcResponse response) {
        ResponseFuture future = unprocessedRequests.get(response.getResponseId());

        if (future != null) {
            future.complete(response);
        } else {
            throw new IllegalStateException();
        }
    }

    public void put(String requestId, ResponseFuture future) {
        unprocessedRequests.put(requestId, future);
    }
}
