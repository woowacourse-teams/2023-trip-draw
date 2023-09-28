package dev.tripdraw.trip.dto;

import static dev.tripdraw.test.fixture.MemberFixture.사용자;
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
class TripSearchResponseOfMemberTest {

    @Test
    void 여행_이미지와_경로_이미지가_null이면_빈값으로_변환해_생성한다() {
        // given
        Member member = 사용자();
        TripName tripName = TripName.from("통후추의 여행");
        Trip trip = new Trip(1L, tripName, member.id(), ONGOING, null, null);

        // when
        TripSearchResponseOfMember response = TripSearchResponseOfMember.from(trip);

        // then
        assertThat(response)
                .usingRecursiveComparison()
                .isEqualTo(TripSearchResponseOfMember.from(new Trip(1L, tripName, member.id(), ONGOING, "", "")));
    }
}
