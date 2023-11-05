package dev.tripdraw.area.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.verify;

import dev.tripdraw.area.domain.Area;
import dev.tripdraw.area.domain.AreaRepository;
import dev.tripdraw.area.dto.AreaResponse;
import dev.tripdraw.area.dto.FullAreaResponse;
import dev.tripdraw.area.dto.FullAreaResponses;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Transactional
@SpringBootTest
class AreaServiceTest {

    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private AreaService areaService;

    @Test
    void 전체_지역을_조회한다() {
        // given
        Area 지역1 = areaRepository.save(new Area("서울시", "강남구", "개포동"));
        Area 지역2 = areaRepository.save(new Area("서울시", "강남구", "강남동"));
        Area 지역3 = areaRepository.save(new Area("서울시", "송파구", "잠실동"));
        Area 지역4 = areaRepository.save(new Area("부산시", "강남구", "개포동"));

        // when
        FullAreaResponses responses = areaService.readAll();

        // then
        assertThat(responses.areas()).usingRecursiveComparison()
                .isEqualTo(
                        List.of(
                                FullAreaResponse.from(지역1),
                                FullAreaResponse.from(지역2),
                                FullAreaResponse.from(지역3),
                                FullAreaResponse.from(지역4)
                        ));
    }

    @MethodSource("paramsAndResponse")
    @ParameterizedTest
    void 해당하는_지역을_조회한다(List<String> sidoAndsigungu, AreaResponse response) {
        // given
        areaRepository.save(new Area("서울시", "강남구", "개포동"));
        areaRepository.save(new Area("서울시", "강남구", "강남동"));
        areaRepository.save(new Area("서울시", "송파구", "잠실동"));
        areaRepository.save(new Area("부산시", "강남구", "개포동"));

        // expect
        assertThat(areaService.read(sidoAndsigungu.get(0), sidoAndsigungu.get(1))).isEqualTo(response);
    }

    @Test
    void 전체_행정구역을_저장한다() {
        // given
        List<Area> areas = List.of(
                new Area("서울시", "강동구", "천호동"),
                new Area("부산시", "부산진구", "부전동")
        );

        // expect
        assertThatNoException().isThrownBy(() -> areaService.create(areas));
    }

    @Test
    void 데이터가_존재하면_행정구역을_저장하지_않는다() {
        // given
        areaRepository.save(new Area("서울시", "강동구", "천호동"));
        areaRepository.save(new Area("부산시", "부산진구", "부전동"));

        // when
        areaService.create(List.of(
                new Area("서울시", "강동구", "성내동")
        ));

        // then
        assertThat(areaRepository.findBySidoAndSigungu("서울시", "강동구"))
                .hasSize(1)
                .containsExactly(new Area("서울시", "강동구", "천호동"));
    }

    private static Stream<Arguments> paramsAndResponse() {
        return Stream.of(
                arguments(List.of("", ""), AreaResponse.from(List.of("부산시", "서울시"))),
                arguments(List.of("서울시", ""), AreaResponse.from(List.of("강남구", "송파구"))),
                arguments(List.of("서울시", "강남구"), AreaResponse.from(List.of("강남동", "개포동")))
        );
    }
}
