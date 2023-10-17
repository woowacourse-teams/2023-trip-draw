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
    class 아무_조건도_갖지_않는지_여부를_확인할_때 {

        @Test
        void 조건이_없다면_true를_반환한다() {
            // given
            TripSearchConditions conditions = TripSearchConditions.builder().build();

            // expect
            assertThat(conditions.hasNoCondition()).isTrue();
        }

        @Test
        void 조건이_있다면_false를_반환한다() {
            // given
            TripSearchConditions conditions = TripSearchConditions.builder()
                    .years(Set.of(2023))
                    .build();

            // expect
            assertThat(conditions.hasNoCondition()).isFalse();
        }
    }

    @Nested
    class 주소_조건만_갖는지_여부를_확인할_때 {

        @Test
        void 주소_조건만_있다면_true를_반환한다() {
            // given
            TripSearchConditions conditions = TripSearchConditions.builder()
                    .address("주소 조건")
                    .build();

            // expect
            assertThat(conditions.hasOnlyAddressCondition()).isTrue();
        }

        @Test
        void 주소_조건이_없다면_false를_반환한다() {
            // given
            TripSearchConditions conditions = TripSearchConditions.builder()
                    .years(Set.of(2023))
                    .build();

            // expect
            assertThat(conditions.hasOnlyAddressCondition()).isFalse();
        }

        @Test
        void 주소_조건과_다른_조건이_있다면_false를_반환한다() {
            // given
            TripSearchConditions conditions = TripSearchConditions.builder()
                    .years(Set.of(2023))
                    .address("주소 조건")
                    .build();

            // expect
            assertThat(conditions.hasOnlyAddressCondition()).isFalse();
        }
    }

    @Nested
    class 시간_조건만_갖는지_여부를_확인할_때 {

        @Test
        void 시간_조건만_있다면_true를_반환한다() {
            // given
            TripSearchConditions conditions = TripSearchConditions.builder()
                    .years(Set.of(2023))
                    .build();

            // expect
            assertThat(conditions.hasOnlyTimeConditions()).isTrue();
        }

        @Test
        void 시간_조건이_없다면_false를_반환한다() {
            // given
            TripSearchConditions conditions = TripSearchConditions.builder()
                    .address("주소 조건")
                    .build();

            // expect
            assertThat(conditions.hasOnlyTimeConditions()).isFalse();
        }

        @Test
        void 시간_조건과_다른_조건이_있다면_false를_반환한다() {
            // given
            TripSearchConditions conditions = TripSearchConditions.builder()
                    .years(Set.of(2023))
                    .address("주소 조건")
                    .build();

            // expect
            assertThat(conditions.hasOnlyTimeConditions()).isFalse();
        }
    }
}
