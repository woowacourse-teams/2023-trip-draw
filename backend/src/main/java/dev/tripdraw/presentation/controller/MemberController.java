package dev.tripdraw.presentation.controller;

import static org.springframework.http.HttpStatus.CREATED;

import dev.tripdraw.application.MemberService;
import dev.tripdraw.dto.member.MemberCreateRequest;
import dev.tripdraw.dto.member.MemberResponse;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
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

    @Operation(summary = "사용자 등록 API", description = "사용자를 등록합니다.", tags = {"사용자", "비로그인"})
    @PostMapping
    public ResponseEntity<MemberResponse> create(@Valid @RequestBody MemberCreateRequest memberCreateRequest) {
        MemberResponse response = memberService.register(memberCreateRequest);

        return ResponseEntity.status(CREATED).body(response);
    }
}
