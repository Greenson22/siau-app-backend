package com.sttis.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    private final String SECRET_KEY = "SistemInformasiAkademikTerintegrasiSecretKeyYangCukupPanjangUntukKeamanan";

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser() // .parserBuilder() menjadi .parser() di versi terbaru
                .verifyWith(getSigningKey()) // .setSigningKey() menjadi .verifyWith()
                .build()
                .parseSignedClaims(token) // .parseClaimsJws() menjadi .parseSignedClaims()
                .getPayload();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    /**
     * Proses pembuatan token JWT dengan API jjwt versi terbaru.
     */
    private String createToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 1000 * 60 * 60 * 10); // Token berlaku 10 jam

        return Jwts.builder()
                .claims(claims)       // Menambahkan semua custom claims dari map
                .subject(subject)     // setSubject() -> subject()
                .issuedAt(now)        // setIssuedAt() -> issuedAt()
                .expiration(expiryDate) // setExpiration() -> expiration()
                .signWith(getSigningKey()) // Hanya perlu key, algoritma otomatis
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = this.SECRET_KEY.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}