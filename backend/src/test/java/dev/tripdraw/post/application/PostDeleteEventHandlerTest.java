package dev.tripdraw.post.application;

import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.times;

import dev.tripdraw.member.domain.MemberDeleteEvent;
import dev.tripdraw.post.domain.PostRepository;
import dev.tripdraw.trip.domain.TripDeleteEvent;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class PostDeleteEventHandlerTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostDeleteEventHandler postDeleteEventHandler;

    @Test
    void 회원_삭제_이벤트를_받아_회원의_감상을_삭제한다() {
        // given
        MemberDeleteEvent memberDeleteEvent = new MemberDeleteEvent(1L);

        // when
        postDeleteEventHandler.deletePostByMemberId(memberDeleteEvent);

        // then
        then(postRepository)
                .should(times(1))
                .deleteByMemberId(1L);
    }

    @Test
    void 여행_삭제_이벤트를_받아_회원의_감상을_삭제한다() {
        // given
        TripDeleteEvent tripDeleteEvent = new TripDeleteEvent(1L);

        // when
        postDeleteEventHandler.deletePostByTripId(tripDeleteEvent);

        // then
        then(postRepository)
                .should(times(1))
                .deleteByTripId(1L);
    }
}
