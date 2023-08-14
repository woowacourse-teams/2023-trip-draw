package dev.tripdraw.aspect;

import static dev.tripdraw.exception.member.MemberExceptionType.MEMBER_NOT_FOUND;

import dev.tripdraw.application.oauth.AuthTokenManager;
import dev.tripdraw.application.oauth.JwtTokenProvider;
import dev.tripdraw.application.oauth.OauthClient;
import dev.tripdraw.application.oauth.OauthClientProvider;
import dev.tripdraw.domain.member.Member;
import dev.tripdraw.domain.member.MemberRepository;
import dev.tripdraw.dto.auth.OauthInfo;
import dev.tripdraw.dto.auth.OauthRequest;
import dev.tripdraw.dto.auth.OauthResponse;
import dev.tripdraw.exception.member.MemberException;
import java.sql.Date;
import java.time.Instant;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TimeTravelToPastWhenLogInAspect {

    @Autowired
    AuthTokenManager authTokenManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    MemberRepository memberRepository;

    private String customTime;
    private final OauthClientProvider oauthClientProvider;

    public TimeTravelToPastWhenLogInAspect(OauthClientProvider oauthClientProvider) {
        this.oauthClientProvider = oauthClientProvider;
    }

    public void timeTravelTo(String customTime) {
        this.customTime = customTime;
    }

    @Around("execution(* dev.tripdraw.application.AuthService.login(..))")
    public Object manipulateTime(ProceedingJoinPoint joinPoint) throws Throwable {
        if (customTime != null) {
            OauthRequest request = (OauthRequest) joinPoint.getArgs()[0];
            OauthClient oauthClient = oauthClientProvider.provide(request.oauthType());
            OauthInfo oauthInfo = oauthClient.requestOauthInfo(request.oauthToken());

            Member member = memberRepository.findByOauthIdAndOauthType(oauthInfo.oauthId(), oauthInfo.oauthType())
                    .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

            Instant manipulatedExpirationTime = Instant.EPOCH;
            String newAccessToken = jwtTokenProvider.generate(
                    String.valueOf(member.id()),
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
