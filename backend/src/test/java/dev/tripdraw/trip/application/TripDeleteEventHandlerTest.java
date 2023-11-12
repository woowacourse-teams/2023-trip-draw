package dev.tripdraw.trip.application;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.times;

import dev.tripdraw.member.domain.MemberDeleteEvent;
import dev.tripdraw.trip.domain.PointRepository;
import dev.tripdraw.trip.domain.TripRepository;
import java.util.List;
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
class TripDeleteEventHandlerTest {

    @Mock
    private TripRepository tripRepository;

    @Mock
    private PointRepository pointRepository;

    @InjectMocks
    private TripDeleteEventHandler tripDeleteEventHandler;

    @Test
    void 회원_삭제_이벤트를_받아_회원의_여행을_삭제한다() {
        // given
        MemberDeleteEvent memberDeleteEvent = new MemberDeleteEvent(1L);
        List<Long> tripIds = List.of(1L);
        given(tripRepository.findAllTripIdsByMemberId(memberDeleteEvent.memberId()))
                .willReturn(List.of(1L));

        // when
        tripDeleteEventHandler.deletePostByMemberId(memberDeleteEvent);

        // then
        then(tripRepository)
                .should(times(1))
                .deleteByMemberId(1L);
        then(pointRepository)
                .should(times(1))
                .deleteByTripIds(tripIds);
    }
}
