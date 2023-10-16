package dev.tripdraw.area.domain;

import static org.assertj.core.api.Assertions.assertThat;

import dev.tripdraw.common.config.JpaConfig;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Import({JpaConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class AreaRepositoryTest {

    @Autowired
    AreaRepository areaRepository;

    @BeforeEach
    void setUp() {
        areaRepository.save(new Area("서울시", "강남구", "개포동"));
        areaRepository.save(new Area("서울시", "강남구", "강남동"));
        areaRepository.save(new Area("서울시", "송파구", "잠실동"));
        areaRepository.save(new Area("부산시", "강남구", "개포동"));
    }

    @Test
    void 시도에_속해있는_시군구를_조회한다() {
        // when
        List<Area> areaOfSeoul = areaRepository.findBySido("서울시");
        List<String> sigungus = areaOfSeoul.stream()
                .map(Area::sigungu)
                .distinct()
                .sorted()
                .toList();

        // then
        assertThat(sigungus).containsExactly("강남구", "송파구");
    }

    @Test
    void 시도와_시군구에_속해있는_읍면동을_조회한다() {
        // when
        List<Area> areasOfSeoulGangnam = areaRepository.findBySidoAndSigungu("서울시", "강남구");
        List<String> umds = areasOfSeoulGangnam.stream()
                .map(Area::umd)
                .distinct()
                .sorted()
                .toList();

        // then
        assertThat(umds).containsExactly("강남동", "개포동");
    }
}
