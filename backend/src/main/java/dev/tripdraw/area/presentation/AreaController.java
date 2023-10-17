package dev.tripdraw.area.presentation;


import static org.springframework.http.HttpStatus.CREATED;

import dev.tripdraw.area.application.AreaService;
import dev.tripdraw.area.dto.AreaReqeust;
import dev.tripdraw.area.dto.AreaResponse;
import dev.tripdraw.common.swagger.SwaggerAuthorizationRequired;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Area", description = "행정구역 관련 API 명세")
@SwaggerAuthorizationRequired
@RequiredArgsConstructor
@RequestMapping("/areas")
@RestController
public class AreaController {

    private final AreaService areaService;

    @Operation(summary = "행정구역 조회 API", description = "행정구역을 조회합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "행정구역 조회 성공."
    )
    @GetMapping
    public ResponseEntity<AreaResponse> read(@RequestBody AreaReqeust areaReqeust) {
        AreaResponse response = areaService.read(areaReqeust);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "행정구역 데이터 생성 API", description = "행정구역 데이터를 생성합니다.")
    @ApiResponse(
            responseCode = "201",
            description = "행정구역 데이터 생성 성공."
    )
    @PostMapping
    public ResponseEntity<AreaResponse> create() {
        AreaResponse response = areaService.create();
        return ResponseEntity.status(CREATED).body(response);
    }
}
