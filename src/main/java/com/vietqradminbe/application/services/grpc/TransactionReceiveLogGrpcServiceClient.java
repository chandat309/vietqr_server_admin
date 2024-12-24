package com.vietqradminbe.application.services.grpc;

import com.example.grpc.GetLogRequest;
import com.example.grpc.TransactionReceiveLogGrpcDTO;
import com.example.grpc.TransactionReceiveLogGrpcDTOList;
import com.example.grpc.TransactionReceiveLogServiceServerGrpc;
import com.vietqradminbe.application.services.grpc.response.TransactionReceiveLogGrpcDTOImpl;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionReceiveLogGrpcServiceClient {
    private static final Logger logger = LoggerFactory.getLogger(TransactionReceiveLogGrpcServiceClient.class);

    @GrpcClient("transaction-receive-log-service")
    private TransactionReceiveLogServiceServerGrpc.TransactionReceiveLogServiceServerBlockingStub blockingStub;

    public List<TransactionReceiveLogGrpcDTOImpl> getTransactionReceiveLogGrpc(GetLogRequest request){
        List<TransactionReceiveLogGrpcDTOImpl> responses = mapToImplList(blockingStub.getTransactionLogsByTransIdGrpc(request));
        return responses;
    }

    public List<TransactionReceiveLogGrpcDTOImpl> mapToImplList(TransactionReceiveLogGrpcDTOList grpcDTOList) {
        List<TransactionReceiveLogGrpcDTOImpl> implList = new ArrayList<>();
        for (TransactionReceiveLogGrpcDTO grpcDTO : grpcDTOList.getTransactionReceiveLogGrpcDTOList()) {
            implList.add(new TransactionReceiveLogGrpcDTOImpl(grpcDTO));  // Assuming constructor or setter mapping
        }
        return implList;
    }

}
