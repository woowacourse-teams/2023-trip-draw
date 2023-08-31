package dev.tripdraw.member.application;

import static dev.tripdraw.domain.oauth.OauthType.KAKAO;
import static dev.tripdraw.member.exception.MemberExceptionType.MEMBER_NOT_FOUND;
import static java.lang.Long.MIN_VALUE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import dev.tripdraw.application.ServiceTest;
import dev.tripdraw.application.oauth.AuthTokenManager;
import dev.tripdraw.member.domain.Member;
import dev.tripdraw.member.domain.MemberRepository;
import dev.tripdraw.post.domain.Post;
import dev.tripdraw.post.domain.PostRepository;
import dev.tripdraw.trip.domain.Point;
import dev.tripdraw.trip.domain.Trip;
import dev.tripdraw.trip.domain.TripName;
import dev.tripdraw.trip.domain.TripRepository;
import dev.tripdraw.member.dto.MemberSearchResponse;
import dev.tripdraw.member.exception.MemberException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AuthTokenManager authTokenManager;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private PostRepository postRepository;

    private Member member;
    private String code;
    private Trip trip;
    private Post post;

    @BeforeEach
    void setUp() {
        member = memberRepository.save(new Member("통후추", "kakaoId", KAKAO));
        code = authTokenManager.generate(member.id());

        trip = tripRepository.save(new Trip(TripName.from("통후추의 여행"), member));
        Point point = new Point(3.14, 5.25, LocalDateTime.now());
        trip.add(point);
        post = postRepository.save(new Post(
                "제목",
                point,
                "위치",
                "오늘은 날씨가 좋네요.",
                member,
                trip.id()
        ));
    }

    @Test
    void code를_입력_받아_사용자를_조회한다() {
        // given & when
        MemberSearchResponse response = memberService.findByCode(code);

        // expect
        assertThat(response).usingRecursiveComparison().isEqualTo(
                new MemberSearchResponse(member.id(), "통후추")
        );
    }

    @Test
    void code를_입력_받아_사용자를_조회할_때_이미_삭제된_사용자라면_예외를_발생시킨다() {
        // given
        Member member = memberRepository.save(new Member("순후추", "kakaoId", KAKAO));
        String code = authTokenManager.generate(member.id());

        memberRepository.deleteById(member.id());

        // expect
        assertThatThrownBy(() -> memberService.findByCode(code))
                .isInstanceOf(MemberException.class)
                .hasMessage(MEMBER_NOT_FOUND.message());
    }

    @Test
    void code를_입력_받아_사용자를_조회할_때_존재하지_않는_사용자라면_예외를_발생시킨다() {
        String nonExistentCode = authTokenManager.generate(MIN_VALUE);

        // expect
        assertThatThrownBy(() -> memberService.findByCode(nonExistentCode))
                .isInstanceOf(MemberException.class)
                .hasMessage(MEMBER_NOT_FOUND.message());
    }

    @Test
    void code를_입력_받아_사용자를_삭제한다() {
        // given & when
        memberService.deleteByCode(code);

        // then
        assertSoftly(softly -> {
            softly.assertThat(memberRepository.findById(member.id())).isEmpty();
            softly.assertThat(tripRepository.findById(trip.id())).isEmpty();
            softly.assertThat(postRepository.findById(post.id())).isEmpty();
        });
    }
}
