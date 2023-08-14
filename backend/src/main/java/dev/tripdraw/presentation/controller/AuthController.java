package dev.tripdraw.presentation.controller;

import dev.tripdraw.application.AuthService;
import dev.tripdraw.dto.auth.OauthRequest;
import dev.tripdraw.dto.auth.OauthResponse;
import dev.tripdraw.dto.auth.RegisterRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth", description = "인증 관련 API 명세")
@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

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
}
