package dev.tripdraw.draw.application;

import static dev.tripdraw.test.fixture.TestFixture.여행;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.timeout;

import dev.tripdraw.trip.domain.TripRepository;
import dev.tripdraw.trip.domain.TripUpdateEvent;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.support.TransactionTemplate;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SpringBootTest
public class TripUpdateEventHandlerIntegrationTest {

    @MockBean
    private RouteImageGenerator routeImageGenerator;

    @MockBean
    private TripRepository tripRepository;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Test
    void 여행수정_이벤트를_발생시키면_이미지를_생성_요청을_한다() {
        // given
        TripUpdateEvent tripUpdateEvent = new TripUpdateEvent(1L);
        given(tripRepository.getTripWithPoints(tripUpdateEvent.tripId()))
                .willReturn(여행());

        // when
        transactionTemplate.executeWithoutResult(action -> applicationEventPublisher.publishEvent(tripUpdateEvent));

        // then
        then(routeImageGenerator)
                .should(timeout(5000).times(1))
                .generate(any(), any(), any(), any());
    }
}
