package com.example.athul.dto;

public class JwtResponse {
    private String token;
    private String role;
    private String userType;

    public JwtResponse(String token, String role, String userType) {
        this.token = token;
        this.role = role;
        this.userType = userType;
    }

    public String getToken() {
        return token;
    }

    public String getRole() {
        return role;
    }

    public String getUserType() {
        return userType;
    }
}
