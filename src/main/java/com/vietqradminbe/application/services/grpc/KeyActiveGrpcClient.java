package com.vietqradminbe.application.services.grpc;

import com.example.grpc.KeyActiveServiceGrpc;
import com.example.grpc.SaveKeyActiveRequest;
import com.example.grpc.SaveKeyActiveResponse;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class KeyActiveGrpcClient {

    @GrpcClient("key-active-service")
    private KeyActiveServiceGrpc.KeyActiveServiceBlockingStub blockingStub;

    public SaveKeyActiveResponse saveKeyActive(SaveKeyActiveRequest request) {
        try {
            SaveKeyActiveResponse response = blockingStub.saveKeyActive(request);
            if (response.getSuccess()) {
                log.info("Successfully saved keyActive via gRPC: {}", request.getKeyActive());
            } else {
                log.warn("Failed to save keyActive via gRPC: {}", response.getMessage());
            }
            return response;
        } catch (Exception e) {
            log.error("Error calling gRPC service: {}", e.getMessage(), e);
            throw new RuntimeException("Error calling gRPC service", e);
        }
    }
}
