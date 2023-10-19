package dev.tripdraw.area.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class AreaTest {

    @Test
    void 전체_주소를_반환한다() {
        // given
        Area area = new Area("서울시", "송파구", "잠실동");

        // when
        String fullAddress = area.toFullAddress();

        // then
        assertThat(fullAddress).isEqualTo("서울시 송파구 잠실동");
    }
}
