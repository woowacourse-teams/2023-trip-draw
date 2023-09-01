package dev.tripdraw.member.presentation;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Member", description = "사용자 관련 API 명세")
@RequiredArgsConstructor
@RequestMapping("/members")
@RestController
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "사용자 조회 API", description = "사용자를 조회합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "사용자 조회 성공."
    )
    @GetMapping
    public ResponseEntity<MemberSearchResponse> findByCode(@RequestParam String code) {
        MemberSearchResponse response = memberService.findByCode(code);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "사용자 삭제 API", description = "사용자를 삭제합니다.")
    @ApiResponse(
            responseCode = "204",
            description = "사용자 삭제 성공."
    )
    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestParam String code) {
        memberService.deleteByCode(code);
        return ResponseEntity.noContent().build();
    }
}
