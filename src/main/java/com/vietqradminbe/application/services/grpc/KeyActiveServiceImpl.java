package com.vietqradminbe.application.services.grpc;

import com.example.grpc.KeyActiveServiceGrpc;
import com.vietqradminbe.domain.models.KeyActiveBankReceive;
import com.vietqradminbe.domain.repositories.KeyActiveBankReceiveRepository;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KeyActiveServiceImpl extends KeyActiveServiceGrpc.KeyActiveServiceImplBase {
    private static final Logger logger = LoggerFactory.getLogger(KeyActiveServiceImpl.class);
    @Autowired
    private KeyActiveBankReceiveRepository keyActiveBankReceiveRepository;

    @Override
    public void updateKeyActive(com.example.grpc.UpdateKeyActiveRequest request, StreamObserver<com.example.grpc.UpdateKeyActiveResponse> responseObserver) {
        logger.info("Received gRPC request: {}", request);

        try {
            // Log dữ liệu được nhận
            String keyActive = request.getKeyActive();
            String bankAccountActivated = request.getBankAccountActivated();
            int status = request.getStatus();

            logger.info("Processing request: keyActive={}, bankAccountActivated={}, status={}", keyActive, bankAccountActivated, status);

            // Tìm kiếm dữ liệu trong cơ sở dữ liệu
            KeyActiveBankReceive keyActiveEntity = keyActiveBankReceiveRepository.findByKeyActive(keyActive);
            if (keyActiveEntity != null) {
                logger.info("Found existing KeyActiveBankReceive entity: {}", keyActiveEntity);

                // Cập nhật dữ liệu
                keyActiveEntity.setBankAccountActivated(bankAccountActivated);
                keyActiveEntity.setStatus(status);
                keyActiveBankReceiveRepository.save(keyActiveEntity);

                logger.info("Updated entity: {}", keyActiveEntity);

                // Tạo phản hồi thành công
                com.example.grpc.UpdateKeyActiveResponse response = com.example.grpc.UpdateKeyActiveResponse.newBuilder()
                        .setSuccess(true)
                        .setMessage("Key active updated successfully")
                        .build();
                logger.info("Sending success response: {}", response);

                responseObserver.onNext(response);
                responseObserver.onCompleted();
            } else {
                logger.warn("KeyActive not found for key: {}", keyActive);

                // Tạo phản hồi không thành công
                com.example.grpc.UpdateKeyActiveResponse response = com.example.grpc.UpdateKeyActiveResponse.newBuilder()
                        .setSuccess(false)
                        .setMessage("Key active not found")
                        .build();
                logger.info("Sending failure response: {}", response);

                responseObserver.onNext(response);
                responseObserver.onCompleted();
            }
        } catch (Exception e) {
            logger.error("Error occurred while processing request: {}", e.getMessage(), e);

            // Tạo phản hồi lỗi
            com.example.grpc.UpdateKeyActiveResponse response = com.example.grpc.UpdateKeyActiveResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage("Error: " + e.getMessage())
                    .build();
            logger.info("Sending error response: {}", response);

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }
}
