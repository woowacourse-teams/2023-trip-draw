package dev.tripdraw.presentation.controller;

import dev.tripdraw.application.MemberService;
import dev.tripdraw.dto.request.MemberCreateRequest;
import dev.tripdraw.dto.response.MemberCreateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/members")
@RestController
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<MemberCreateResponse> create(@RequestBody MemberCreateRequest memberCreateRequest) {
        MemberCreateResponse response = memberService.register(memberCreateRequest);

        return ResponseEntity
                .status(HttpStatusCode.valueOf(201))
                .body(response);
    }
}
