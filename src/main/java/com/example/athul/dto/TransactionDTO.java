package com.example.athul.dto;

import java.time.LocalDateTime;

public class TransactionDTO {
    private Long transactionId;
    private String userName;
    private Double amount;
    private String status;
    private LocalDateTime date;
    private String paymentMethod;

    public TransactionDTO(Long transactionId, String userName, Double amount, String status, LocalDateTime date, String paymentMethod) {
        this.transactionId = transactionId;
        this.userName = userName;
        this.amount = amount;
        this.status = status;
        this.date = date;
        this.paymentMethod = paymentMethod;
    }

    // Getters and Setters

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
