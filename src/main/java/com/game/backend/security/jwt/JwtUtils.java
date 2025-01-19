package com.game.backend.security.jwt;

import com.game.backend.models.User;
import com.game.backend.repositories.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class JwtUtils {
    @Autowired
    UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    private final Key key;

    @Value("${spring.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    public JwtUtils(@Value("${spring.app.jwtSecret}") String jwtSecret) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getJwtFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public String getJwtFromCookies(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("access_token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public ResponseCookie generateJwtCookie(String username, List<String> roles) {
        Optional<User> optionalUser = userRepository.findByUserName(username);

        String email = "";
        if (optionalUser.isPresent()) {
            User currentUser = optionalUser.get();
            email = currentUser.getEmail();
        }

        String jwtToken = generateTokenFromUsername(username, email, roles);

        return ResponseCookie.from("access_token", jwtToken)
                .httpOnly(false)
                .secure(true)
                .path("/")
                .maxAge(7 * 24 * 60 * 60) // 7 days
                .sameSite("Strict")
                .build();
    }


    public String generateTokenFromUsername(String username, String email, List<String> roles) {
        return Jwts.builder()
                .subject(username)
                .claim("roles", roles)
                .claim("email", email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) key)
                .build().parseSignedClaims(token)
                .getPayload().getSubject();
    }

    public String getEmailFromJwtToken(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) key)
                .build().parseSignedClaims(token)
                .getPayload()
                .get("email", String.class);
    }


    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().verifyWith((SecretKey) key).build().parseSignedClaims(authToken);
            return true;
        } catch (JwtException e) {
            logger.warn("JWT validation failed: {}", e.getMessage());
        }
        return false;
    }

    public ResponseCookie generateCleanJwtCookie() {
        return ResponseCookie.from("access_token", "")
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .maxAge(0)
                .path("/")
                .build();
    }

    public List<String> getRolesFromJwtToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith((SecretKey) key)
                .build().parseSignedClaims(token)
                .getPayload();
        return claims.get("roles", List.class);
    }

}

