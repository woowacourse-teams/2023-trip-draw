package dev.tripdraw.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.tripdraw.domain.member.Member;
import dev.tripdraw.domain.member.MemberRepository;
import dev.tripdraw.domain.trip.Trip;
import dev.tripdraw.domain.trip.TripRepository;
import dev.tripdraw.dto.LoginUser;
import dev.tripdraw.dto.request.PointCreateRequest;
import dev.tripdraw.dto.response.PointCreateResponse;
import dev.tripdraw.exception.BadRequestException;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Transactional
@SpringBootTest
class TripServiceTest {

    @Autowired
    private TripService tripService;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Trip trip;
    private LoginUser loginUser;

    @BeforeEach
    void setUp() {
        Member member = memberRepository.save(new Member("통후추"));
        trip = tripRepository.save(new Trip("통후추의 여행", member));
        loginUser = new LoginUser("통후추");
    }

    @Test
    void 여행에_위치_정보를_추가한다() {
        // given
        PointCreateRequest pointCreateRequest = new PointCreateRequest(
                trip.getId(),
                1.1,
                2.2,
                LocalDateTime.now()
        );

        // when
        PointCreateResponse pointCreateResponse = tripService.addPoint(loginUser, pointCreateRequest);

        // then
        assertThat(pointCreateResponse.id()).isNotNull();
    }

    @Test
    void 여행에_위치_정보를_추가할_때_해당_여행이_존재하지_않으면_예외를_발생시킨다() {
        // given
        Long nonExistentId = Long.MIN_VALUE;
        PointCreateRequest pointCreateRequest = new PointCreateRequest(
                nonExistentId,
                1.1,
                2.2,
                LocalDateTime.now()
        );

        // expect
        assertThatThrownBy(() -> tripService.addPoint(loginUser, pointCreateRequest))
                .isInstanceOf(BadRequestException.class);
    }
}
