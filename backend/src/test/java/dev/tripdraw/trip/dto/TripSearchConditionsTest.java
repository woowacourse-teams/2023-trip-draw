package dev.tripdraw.trip.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class TripSearchConditionsTest {

    @Nested
    class 조건이_충족되었을_때 {

        @Test
        void 모든_조건을_갖는지_확인한다() {
            // given
            TripSearchConditions tripSearchConditions = TripSearchConditions.builder()
                    .years(Set.of(1))
                    .address("address")
                    .build();

            // expect
            assertThat(tripSearchConditions.hasAllConditions()).isTrue();
        }

        @Test
        void 주소_조건을_갖는지_확인한다() {
            // given
            TripSearchConditions tripSearchConditions = TripSearchConditions.builder()
                    .address("address")
                    .build();

            // expect
            assertThat(tripSearchConditions.hasAddressCondition()).isTrue();
        }

        @Test
        void 시간_조건을_갖는지_확인한다() {
            // given
            TripSearchConditions tripSearchConditions = TripSearchConditions.builder()
                    .years(Set.of(1))
                    .build();

            // expect
            assertThat(tripSearchConditions.hasTimeConditions()).isTrue();
        }
    }

    @Nested
    class 조건이_충족되지_않았을_때 {

        @Test
        void 모든_조건을_갖는지_확인한다() {
            // given
            TripSearchConditions tripSearchConditions = TripSearchConditions.builder()
                    .build();

            // expect
            assertThat(tripSearchConditions.hasAllConditions()).isFalse();
        }

        @Test
        void 주소_조건을_갖는지_확인한다() {
            // given
            TripSearchConditions tripSearchConditions = TripSearchConditions.builder()
                    .build();

            // expect
            assertThat(tripSearchConditions.hasAddressCondition()).isFalse();
        }

        @Test
        void 시간_조건을_갖는지_확인한다() {
            // given
            TripSearchConditions tripSearchConditions = TripSearchConditions.builder()
                    .build();

            // expect
            assertThat(tripSearchConditions.hasTimeConditions()).isFalse();
        }
    }
}
