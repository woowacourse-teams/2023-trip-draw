package dev.tripdraw.auth.application;

import static dev.tripdraw.auth.exception.AuthExceptionType.EXPIRED_TOKEN;
import static dev.tripdraw.auth.exception.AuthExceptionType.INVALID_TOKEN;

import dev.tripdraw.auth.config.AccessTokenConfig;
import dev.tripdraw.auth.exception.AuthException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    private final Key accessKey;
    private final Long accessTokenExpirationTime;

    public JwtTokenProvider(AccessTokenConfig accessTokenConfig) {
        byte[] keyBytes = Decoders.BASE64.decode(accessTokenConfig.secretKey());
        this.accessKey = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpirationTime = accessTokenConfig.expirationTime();
    }

    public String generateAccessToken(String subject) {
        final Date now = new Date();
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessTokenExpirationTime))
                .signWith(accessKey, SignatureAlgorithm.HS512)
                .compact();
    }

    public String extractAccessToken(String accessToken) {
        Claims claims = parseClaims(accessToken, accessKey);
        return claims.getSubject();
    }

    private Claims parseClaims(String token, Key key) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new AuthException(EXPIRED_TOKEN);
        } catch (JwtException e) {
            throw new AuthException(INVALID_TOKEN);
        }
    }
}
