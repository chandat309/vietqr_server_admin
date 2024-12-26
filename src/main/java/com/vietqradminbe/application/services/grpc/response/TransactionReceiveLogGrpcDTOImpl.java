package com.vietqradminbe.application.services.grpc.response;

import com.example.grpc.TransactionReceiveLogGrpcDTO;

public class TransactionReceiveLogGrpcDTOImpl {
    private String id;
    private int type;
    private String transactionId;
    private String status;
    private int statusCode;
    private String message;
    private String urlCallBack;
    private long timeRequest;
    private long timeResponse;

    // Constructor to map from TransactionReceiveLogGrpcDTO
    public TransactionReceiveLogGrpcDTOImpl(TransactionReceiveLogGrpcDTO grpcDTO) {
        this.id = grpcDTO.getId();
        this.type = grpcDTO.getType();
        this.transactionId = grpcDTO.getTransactionId();
        this.status = grpcDTO.getStatus();
        this.statusCode = grpcDTO.getStatusCode();
        this.message = grpcDTO.getMessage();
        this.urlCallBack = grpcDTO.getUrlCallBack();
        this.timeRequest = grpcDTO.getTimeRequest();
        this.timeResponse = grpcDTO.getTimeResponse();
    }

    // Getters and setters for the fields

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrlCallBack() {
        return urlCallBack;
    }

    public void setUrlCallBack(String urlCallBack) {
        this.urlCallBack = urlCallBack;
    }

    public long getTimeRequest() {
        return timeRequest;
    }

    public void setTimeRequest(long timeRequest) {
        this.timeRequest = timeRequest;
    }

    public long getTimeResponse() {
        return timeResponse;
    }

    public void setTimeResponse(long timeResponse) {
        this.timeResponse = timeResponse;
    }
}
