package dev.tripdraw.draw.application;

import static dev.tripdraw.test.TestFixture.여행;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import dev.tripdraw.draw.application.RouteImageGenerator;
import dev.tripdraw.draw.application.TripUpdateEventHandler;
import dev.tripdraw.trip.domain.TripRepository;
import dev.tripdraw.trip.domain.TripUpdateEvent;
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
class TripUpdateEventHandlerTest {

    @Mock
    private RouteImageGenerator routeImageGenerator;

    @Mock
    private TripRepository tripRepository;

    @InjectMocks
    private TripUpdateEventHandler tripUpdateEventHandler;

    @Test
    void 여행수정_이벤트를_받아_이미지를_생성_요청을_한다() {
        // given
        TripUpdateEvent tripUpdateEvent = new TripUpdateEvent(1L);
        given(tripRepository.getTripWithPoints(tripUpdateEvent.tripId()))
                .willReturn(여행());

        // when
        tripUpdateEventHandler.handle(tripUpdateEvent);

        // then
        then(routeImageGenerator)
                .should(times(1))
                .generate(any(), any(), any(), any());
    }
}
