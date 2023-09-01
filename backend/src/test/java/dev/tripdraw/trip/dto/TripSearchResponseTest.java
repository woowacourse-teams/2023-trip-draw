package dev.tripdraw.trip.dto;

import static dev.tripdraw.auth.domain.OauthType.KAKAO;
import static dev.tripdraw.trip.domain.TripStatus.ONGOING;
import static org.assertj.core.api.Assertions.assertThat;

import dev.tripdraw.member.domain.Member;
import dev.tripdraw.trip.domain.Trip;
import dev.tripdraw.trip.domain.TripName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class TripSearchResponseTest {

    @Test
    void 여행_이미지와_경로_이미지가_null이면_빈값으로_변환해_생성한다() {
        // given
        Member member = new Member("통후추", "kakaoId", KAKAO);
        TripName tripName = TripName.from("통후추의 여행");
        Trip trip = new Trip(1L, tripName, member, ONGOING, null, null);

        // when
        TripSearchResponse response = TripSearchResponse.from(trip);

        // then
        assertThat(response).usingRecursiveComparison().isEqualTo(
                TripSearchResponse.from(new Trip(1L, tripName, member, ONGOING, "", ""))
        );
    }
}
