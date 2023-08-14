package dev.tripdraw.aspect;

import dev.tripdraw.application.oauth.AuthTokenManager;
import dev.tripdraw.application.oauth.JwtTokenProvider;
import dev.tripdraw.dto.auth.AccessTokenRefreshRequest;
import dev.tripdraw.dto.auth.OauthResponse;
import java.sql.Date;
import java.time.Instant;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TimeTravelToFutureWhenRefreshAspect {

    @Autowired
    AuthTokenManager authTokenManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    private String customTime;

    public void setCustomTime(String customTime) {
        this.customTime = customTime;
    }

    @Around("execution(* dev.tripdraw.application.AuthService.refresh(..))")
    public Object manipulateTime(ProceedingJoinPoint joinPoint) throws Throwable {
        if (customTime != null) {
            AccessTokenRefreshRequest request = (AccessTokenRefreshRequest) joinPoint.getArgs()[0];
            Long memberId = authTokenManager.extractMemberId(request.accessToken());

            Instant manipulatedExpirationTime = Instant.now().plusSeconds(60);
            String newAccessToken = jwtTokenProvider.generate(
                    String.valueOf(memberId),
                    Date.from(manipulatedExpirationTime)
            );

            OauthResponse manipulatedResponse = new OauthResponse(newAccessToken);

            Object originalResponse = joinPoint.proceed();
            if (originalResponse instanceof OauthResponse) {
                return manipulatedResponse;
            } else {
                return originalResponse;
            }
        }

        return joinPoint.proceed();
    }
}
