package dev.tripdraw.presentation.controller;

import dev.tripdraw.application.MemberService;
import dev.tripdraw.dto.request.MemberCreateRequest;
import java.net.URI;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<Void> create(@RequestBody MemberCreateRequest memberCreateRequest) {
        Long id = memberService.register(memberCreateRequest);

        return ResponseEntity.created(URI.create(String.valueOf(id))).build();
    }
}
