package com.example.athul.dto;

public class ResetPasswordRequest {
    private String token;
    private String newPassword;

    // Default Constructor (Required for JSON deserialization)
    public ResetPasswordRequest() {
    }

    // Parameterized Constructor (Optional)
    public ResetPasswordRequest(String token, String newPassword) {
        this.token = token;
        this.newPassword = newPassword;
    }

    // Getters
    public String getToken() {
        return token;
    }

    public String getNewPassword() {
        return newPassword;
    }

    // Setters
    public void setToken(String token) {
        this.token = token;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
