package dev.tripdraw.domain.trip;

import static dev.tripdraw.domain.oauth.OauthType.KAKAO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import dev.tripdraw.domain.member.Member;
import dev.tripdraw.domain.member.MemberRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DataJpaTest
class TripRepositoryTest {

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Member member;

    @BeforeEach
    void setUp() {
        member = memberRepository.save(new Member("통후추", "kakaoId", KAKAO));
    }

    @Test
    void 회원_ID로_여행_목록을_조회한다() {
        // given
        Trip trip = new Trip(TripName.from("제주도 여행"), member);
        tripRepository.save(trip);

        // when
        List<Trip> trips = tripRepository.findAllByMemberId(member.id());

        // then
        assertSoftly(softly -> {
            softly.assertThat(trips).hasSize(1);
            softly.assertThat(trips.get(0)).isEqualTo(trip);
        });
    }

    @Test
    void 회원_ID로_여행_목록을_조회할_때_해당_회원의_여행이_없다면_빈_여행_목록을_반환한다() {
        // given & when
        List<Trip> trips = tripRepository.findAllByMemberId(member.id());

        // then
        assertThat(trips).hasSize(0);
    }

    @Test
    void 회원_ID로_여행을_삭제한다() {
        // given
        Trip trip = new Trip(TripName.from("제주도 여행"), member);
        tripRepository.save(trip);

        // when
        tripRepository.deleteByMemberId(member.id());

        // then
        assertThat(tripRepository.findById(trip.id())).isEmpty();
    }
}
