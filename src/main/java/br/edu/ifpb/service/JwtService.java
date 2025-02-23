package br.edu.ifpb.service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;

@Service
public class JwtService {
	
	@Value("${jwtSecret}")
	private String jwtSecret;//jwtSecret;

	@Value("${jwtExpirationMs}")
	private int jwtExpirationMs;
	
//	private String SECRET_KEY = "seuSegredoAqui"; // Substituir por algo seguro

//	private Key getSigningKey() {
//        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
//        return Keys.hmacShaKeyFor(keyBytes);
//    }
	
	private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders
        		.BASE64
        		.decode(new String(jwtSecret.getBytes(StandardCharsets.UTF_8)));
        return new SecretKeySpec(keyBytes, "HmacSHA256");
    }

	public String generateToken(Authentication authentication) {
    	return Jwts.builder()
                .subject(authentication.getName())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs)) // 1 hora de validade
                .signWith(getSigningKey()) // Assina o token com a chave segura
                .compact();
    }
	
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
//        return Jwts.parser()
//                .verifyWith(getSigningKey())
//                .build()
//                .parseSignedClaims(token)
//                .getPayload()
//                .getSubject(); // Obtém o username do JWT
    }

	public boolean validateJwtToken(String token, String username) {
		try {
			Jwts.parser()
				.verifyWith(getSigningKey())
				.build()
				.parse(token);

			return extractUsername(token).equals(username);
		} catch (MalformedJwtException e) {
			System.out.println("Invalid JWT token: {}" + e.getMessage());
		} catch (ExpiredJwtException e) {
			System.out.println("JWT token is expired: {}" + e.getMessage());
		} catch (UnsupportedJwtException e) {
			System.out.println("JWT token is unsupported: {}" + e.getMessage());
		} catch (IllegalArgumentException e) {
			System.out.println("JWT claims string is empty: {}" + e.getMessage());
		}

		return false;
	}

	private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = Jwts.parser()
        		.verifyWith(getSigningKey())
        		.build()
        		.parseSignedClaims(token)
        		.getPayload();
        return claimsResolver.apply(claims);
    }

//
//    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
//        Claims claims = Jwts.parser()
//                .setSigningKey(SECRET_KEY)
//                .parseClaimsJws(token)
//                .getBody();
//        return claimsResolver.apply(claims);
//    }
//
//    public boolean validateToken(String token, String username) {
//        return extractUsername(token).equals(username) && !isTokenExpired(token);
//    }
//
//    private boolean isTokenExpired(String token) {
//        return extractClaim(token, Claims::getExpiration).before(new Date());
//    }
}