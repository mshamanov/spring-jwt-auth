package dev.mash.jwtauth.service;

import dev.mash.jwtauth.entity.Role;
import dev.mash.jwtauth.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Service to manage JWT tokens.
 *
 * @author Mikhail Shamanov
 */
@Service
public class JwtService {
    private final SecretKey secretKey;

    private final Duration expiration;

    public JwtService(@Value("${jwt.token.expiration:24h}") Duration expiration) {
        this.secretKey = this.getSigningKey();
        this.expiration = expiration;
    }

    /**
     * Generates JWT token
     *
     * @param userDetails user data
     * @return JWT token
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        if (userDetails instanceof User customUserDetails) {
            claims.put("id", customUserDetails.getId());
            claims.put("email", customUserDetails.getEmail());
            claims.put("roles", customUserDetails.getRoles().stream().map(Role::getType).toArray());
        }
        return this.generateToken(claims, userDetails);
    }

    /**
     * Generates JWT token
     *
     * @param claims      additional data
     * @param userDetails user data
     * @return JWT token
     */
    private String generateToken(Map<String, Object> claims, UserDetails userDetails) {
        final Date date = new Date();
        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(date)
                .expiration(new Date(date.getTime() + this.expiration.toMillis()))
                .signWith(this.secretKey)
                .compact();
    }

    /**
     * Checks whether JWT token is valid
     *
     * @param token JWT token
     * @return true if token is valid
     */
    public boolean isTokenValid(String token) {
        return !this.isTokenExpired(token);
    }

    /**
     * Checks whether JWT token is expired
     *
     * @param token JWT token
     * @return true if token is expired
     */
    private boolean isTokenExpired(String token) {
        return this.extractExpiration(token).before(new Date());
    }

    /**
     * Extracts username from JWT token
     *
     * @param token JWT token
     * @return username
     */
    public String extractUserName(String token) {
        return this.extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts username from JWT token
     *
     * @param token JWT token
     * @return username
     */
    public List<?> extractRoles(String token) {
        return this.extractAllClaims(token).get("roles", List.class);
    }

    /**
     * Extracts expiration date from JWT token
     *
     * @param token JWT token
     * @return expiration date
     */
    public Date extractExpiration(String token) {
        return this.extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts specific claim value via specified resolver
     *
     * @param token    JWT token
     * @param resolver function that accepts claims and returns extracted claim value
     * @return claim value
     */
    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        final Claims claims = this.extractAllClaims(token);
        return resolver.apply(claims);
    }

    /**
     * Extracts all the claims from JWT token
     *
     * @param token JWT token
     * @return all claims
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(this.secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Generates secret key to sign JWT token
     *
     * @return secret key
     */
    private SecretKey getSigningKey() {
        return Jwts.SIG.HS256.key().build();
    }
}
