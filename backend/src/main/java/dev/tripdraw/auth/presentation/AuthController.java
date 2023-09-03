package dev.tripdraw.auth.presentation;

import dev.tripdraw.auth.application.AuthService;
import dev.tripdraw.auth.dto.OauthRequest;
import dev.tripdraw.auth.dto.OauthResponse;
import dev.tripdraw.auth.dto.RegisterRequest;
import dev.tripdraw.auth.dto.TokenRefreshRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth", description = "인증 관련 API 명세")
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "소셜 로그인 API", description = "소셜 로그인을 합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "로그인 성공"
    )
    @PostMapping("/oauth/login")
    public ResponseEntity<OauthResponse> login(@RequestBody OauthRequest oauthRequest) {
        OauthResponse oauthResponse = authService.login(oauthRequest);
        return ResponseEntity.ok(oauthResponse);
    }

    @Operation(summary = "닉네임 등록 API", description = "신규 회원의 닉네임을 등록합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "닉네임 등록 성공."
    )
    @PostMapping("/oauth/register")
    public ResponseEntity<OauthResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        OauthResponse oauthResponse = authService.register(registerRequest);
        return ResponseEntity.ok(oauthResponse);
    }

    @Operation(summary = "토큰 재발급 API", description = "리프레쉬 토큰을 이용하여 토큰을 재발급합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "토큰 재발급 성공."
    )
    @PostMapping("/oauth/refresh")
    public ResponseEntity<OauthResponse> refresh(@Valid @RequestBody TokenRefreshRequest tokenRefreshRequest) {
        OauthResponse oauthResponse = authService.refresh(tokenRefreshRequest);
        return ResponseEntity.ok(oauthResponse);
    }
}
