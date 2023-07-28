package dev.tripdraw.presentation.controller;

import static org.springframework.http.HttpStatus.CREATED;

import dev.tripdraw.application.MemberService;
import dev.tripdraw.dto.member.MemberCreateRequest;
import dev.tripdraw.dto.member.MemberCreateResponse;
import dev.tripdraw.dto.member.MemberSearchResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Member", description = "사용자 관련 API 명세")
@RequestMapping("/members")
@RestController
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @Operation(summary = "사용자 등록 API", description = "사용자를 등록합니다.")
    @ApiResponse(
            responseCode = "201",
            description = "사용자 등록 성공."
    )
    @PostMapping
    public ResponseEntity<MemberCreateResponse> create(@Valid @RequestBody MemberCreateRequest memberCreateRequest) {
        MemberCreateResponse response = memberService.register(memberCreateRequest);
        return ResponseEntity.status(CREATED).body(response);
    }

    @Operation(summary = "사용자 조회 API", description = "사용자를 조회합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "사용자 조회 성공."
    )
    @GetMapping("/{memberId}")
    public ResponseEntity<MemberSearchResponse> findById(@PathVariable Long memberId) {
        MemberSearchResponse response = memberService.findById(memberId);
        return ResponseEntity.ok(response);
    }
}
