package com.example.athul.dto;

public class PaymentDTO {

    private Double amount;
    private String paymentMethod;
    private String upiId;
    private String cardNumber;
    private String cardHolderName;
    private String bankName;
    private Long freelancerId;
    private String freelancerFullName;  // Add freelancer's full name

    // Constructor to initialize PaymentDTO
    public PaymentDTO(Double amount, String paymentMethod, String upiId, String cardNumber, 
                      String cardHolderName, String bankName, Long freelancerId, String freelancerFullName) {
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.upiId = upiId;
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
        this.bankName = bankName;
        this.freelancerId = freelancerId;
        this.freelancerFullName = freelancerFullName;  // Initialize freelancer's full name
    }

    // Getters and setters for all fields
    public String getFreelancerFullName() {
        return freelancerFullName;
    }

    public void setFreelancerFullName(String freelancerFullName) {
        this.freelancerFullName = freelancerFullName;
    }

    // Getters and setters for all fields
    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getUpiId() {
        return upiId;
    }

    public void setUpiId(String upiId) {
        this.upiId = upiId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public Long getFreelancerId() {
        return freelancerId;
    }

    public void setFreelancerId(Long freelancerId) {
        this.freelancerId = freelancerId;
    }

    // Manual validation methods
    public boolean isValid() {
        if (amount == null || amount <= 0) {
            return false;
        }
        if (paymentMethod == null || paymentMethod.isEmpty()) {
            return false;
        }
        if (freelancerId == null || freelancerId <= 0) {
            return false;
        }

        // Validate payment method-specific fields
        if (paymentMethod.equals("UPI") && (upiId == null || upiId.isEmpty())) {
            return false;
        }

        if (paymentMethod.equals("Credit Card")) {
            if (cardNumber == null || cardNumber.length() != 16) {
                return false;
            }
            if (cardHolderName == null || cardHolderName.isEmpty()) {
                return false;
            }
        }

        if (paymentMethod.equals("Net Banking") && (bankName == null || bankName.isEmpty())) {
            return false;
        }

        return true;
    }
}
