package dev.tripdraw.member.presentation;

import dev.tripdraw.common.auth.Auth;
import dev.tripdraw.common.auth.LoginUser;
import dev.tripdraw.common.swagger.SwaggerAuthorizationRequired;
import dev.tripdraw.member.application.MemberService;
import dev.tripdraw.member.dto.MemberSearchResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Member", description = "사용자 관련 API 명세")
@RequiredArgsConstructor
@SwaggerAuthorizationRequired
@RequestMapping("/members")
@RestController
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "사용자 조회 API", description = "사용자를 조회합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "사용자 조회 성공."
    )
    @GetMapping("/me")
    public ResponseEntity<MemberSearchResponse> find(@Auth LoginUser loginUser) {
        MemberSearchResponse response = memberService.find(loginUser);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "사용자 삭제 API", description = "사용자를 삭제합니다.")
    @ApiResponse(
            responseCode = "204",
            description = "사용자 삭제 성공."
    )
    @DeleteMapping("/me")
    public ResponseEntity<Void> delete(@Auth LoginUser loginUser) {
        memberService.delete(loginUser);
        return ResponseEntity.noContent().build();
    }
}
