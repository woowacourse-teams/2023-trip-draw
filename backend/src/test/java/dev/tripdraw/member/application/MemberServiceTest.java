package dev.tripdraw.member.application;

import static dev.tripdraw.test.fixture.MemberFixture.사용자;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import dev.tripdraw.common.auth.LoginUser;
import dev.tripdraw.member.domain.Member;
import dev.tripdraw.member.domain.MemberDeleteEvent;
import dev.tripdraw.member.domain.MemberRepository;
import dev.tripdraw.member.dto.MemberSearchResponse;
import dev.tripdraw.test.ServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;

@RecordApplicationEvents
@ServiceTest
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private ApplicationEvents applicationEvents;

    @Autowired
    private MemberRepository memberRepository;

    private Member member;

    @BeforeEach
    void setUp() {
        member = memberRepository.save(사용자());
    }

    @Test
    void 사용자를_조회한다() {
        // given
        LoginUser loginUser = new LoginUser(member.id());

        // when
        MemberSearchResponse response = memberService.find(loginUser);

        // then
        assertThat(response)
                .usingRecursiveComparison()
                .isEqualTo(new MemberSearchResponse(member.id(), "통후추"));
    }

    @Test
    void 사용자를_삭제한다() {
        // given
        LoginUser loginUser = new LoginUser(member.id());

        // when
        memberService.delete(loginUser);

        // then
        long publishedEvents = applicationEvents.stream(MemberDeleteEvent.class)
                .filter(event -> event.memberId().equals(member.id()))
                .count();

        assertSoftly(softly -> {
            softly.assertThat(memberRepository.findById(member.id())).isEmpty();
            softly.assertThat(publishedEvents).isOne();
        });
    }
}
