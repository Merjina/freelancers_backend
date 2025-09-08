package com.example.athul.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    private static final String SECRET_KEY_STRING = "kL+5NT3jfmP+PNdUxvfma2TE+LvXr8Ueqrc2JxImoNBK9osqJpnFAMJE9p7idGoz5kPji6PCAWYl63uH48aThg==";
    private final Key SECRET_KEY = Keys.hmacShaKeyFor(Base64.getDecoder().decode(SECRET_KEY_STRING));

    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours validity
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        System.out.println("🧪 JWT username: " + username);
        System.out.println("🧪 UserDetails username: " + userDetails.getUsername());
        System.out.println("🧪 Token expired? " + isTokenExpired(token));

        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    private boolean isTokenExpired(String token) {
        Date expirationDate = extractClaim(token, Claims::getExpiration);
        System.out.println("🧪 Token expiration date: " + expirationDate);
        return expirationDate.before(new Date());
    }

	public boolean validateToken(String token, String username) {
		// TODO Auto-generated method stub
		return false;
	}

}
