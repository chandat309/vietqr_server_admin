package com.vietqradminbe.application.services.grpc;

import com.example.grpc.*;
import com.vietqradminbe.application.services.grpc.response.TransactionReceiveLogGrpcDTOImpl;
import com.vietqradminbe.application.services.grpc.response.TransactionRefundGrpcDTOImpl;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionRefundServiceClient {
    private static final Logger logger = LoggerFactory.getLogger(TransactionRefundServiceClient.class);

    @GrpcClient("transaction-refund-service")
    private TransactionRefundServiceServerGrpc.TransactionRefundServiceServerBlockingStub blockingStub;

    public List<TransactionRefundGrpcDTOImpl> getTransactionRefundGrpc(GetTransactionRefundRequest request){
        List<TransactionRefundGrpcDTOImpl> responses = mapToImplList(blockingStub.getTransactionRefundAdminDetail(request));
        return responses;
    }

    public List<TransactionRefundGrpcDTOImpl> mapToImplList(TransactionRefundGrpcDTOList grpcDTOList) {
        List<TransactionRefundGrpcDTOImpl> implList = new ArrayList<>();
        for (TransactionRefundAdminDetailDTO grpcDTO : grpcDTOList.getTransactionRefundGrpcDTOList()) {
            implList.add(new TransactionRefundGrpcDTOImpl(grpcDTO));  // Assuming constructor or setter mapping
        }
        return implList;
    }
}
