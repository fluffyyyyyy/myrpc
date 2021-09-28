package org.example.transport.entity;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ResponseFuture {
    private CompletableFuture<RpcResponse> resultFuture;

    public ResponseFuture(CompletableFuture<RpcResponse> future) {
        this.resultFuture = future;
    }

    public void complete(RpcResponse response) {
        resultFuture.complete(response);
    }

    public RpcResponse get() {
        try {
            return resultFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

}
