package com.vietqradminbe.application.services.grpc.response;

import com.example.grpc.TransactionRefundAdminDetailDTO;

public class TransactionRefundGrpcDTOImpl {
    private int status;
    private int amount;
    private String referenceNumber;
    private String orderId;

    // Constructor to map from TransactionRefundGrpcDTO
    public TransactionRefundGrpcDTOImpl(TransactionRefundAdminDetailDTO grpcDTO) {
        this.status = grpcDTO.getStatus();
        this.amount = grpcDTO.getAmount();
        this.referenceNumber = grpcDTO.getReferenceNumber();
        this.orderId = grpcDTO.getOrderId();
    }

    // Getters and setters for the fields

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}

