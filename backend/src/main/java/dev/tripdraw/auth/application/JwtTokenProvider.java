package dev.tripdraw.auth.application;

import static dev.tripdraw.auth.exception.AuthExceptionType.EXPIRED_ACCESS_TOKEN;
import static dev.tripdraw.auth.exception.AuthExceptionType.EXPIRED_REFRESH_TOKEN;
import static dev.tripdraw.auth.exception.AuthExceptionType.INVALID_TOKEN;

import dev.tripdraw.auth.config.AccessTokenConfig;
import dev.tripdraw.auth.config.RefreshTokenConfig;
import dev.tripdraw.auth.exception.AuthException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    private final Key accessKey;
    private final Long accessTokenExpirationTime;
    private final Key refreshKey;
    private final Long refreshTokenExpirationTime;

    public JwtTokenProvider(AccessTokenConfig accessTokenConfig, RefreshTokenConfig refreshTokenConfig) {
        byte[] accessKeyBytes = Decoders.BASE64.decode(accessTokenConfig.secretKey());
        this.accessKey = Keys.hmacShaKeyFor(accessKeyBytes);
        this.accessTokenExpirationTime = accessTokenConfig.expirationTime();
        byte[] refreshKeyBytes = Decoders.BASE64.decode(refreshTokenConfig.secretKey());
        this.refreshKey = Keys.hmacShaKeyFor(refreshKeyBytes);
        this.refreshTokenExpirationTime = refreshTokenConfig.expirationTime();
    }

    public String generateAccessToken(String subject) {
        return generateToken(subject, accessTokenExpirationTime, accessKey);
    }

    private String generateToken(String subject, Long expirationTime, Key key) {
        final Date now = new Date();
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expirationTime))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String generateRefreshToken() {
        return generateToken(UUID.randomUUID().toString(), refreshTokenExpirationTime, refreshKey);
    }

    public String extractAccessToken(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(accessKey)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody()
                    .getSubject();
        } catch (ExpiredJwtException e) {
            throw new AuthException(EXPIRED_ACCESS_TOKEN);
        } catch (JwtException e) {
            throw new AuthException(INVALID_TOKEN);
        }
    }

    public void validateRefreshToken(String refreshToken) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(refreshKey)
                    .build()
                    .parseClaimsJws(refreshToken);
        } catch (ExpiredJwtException e) {
            throw new AuthException(EXPIRED_REFRESH_TOKEN);
        } catch (JwtException e) {
            throw new AuthException(INVALID_TOKEN);
        }
    }
}
