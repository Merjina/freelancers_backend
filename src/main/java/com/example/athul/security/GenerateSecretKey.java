package com.example.athul.security;


import java.util.Base64;
import javax.crypto.SecretKey;
import io.jsonwebtoken.security.Keys;

public class GenerateSecretKey {
    public static void main(String[] args) {
        SecretKey key = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS512);
        String base64Key = Base64.getEncoder().encodeToString(key.getEncoded());
        System.out.println("Generated Secret Key: " + base64Key);
    }
}
