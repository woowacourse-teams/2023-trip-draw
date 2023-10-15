package dev.tripdraw.draw.application;

import static dev.tripdraw.test.fixture.PostFixture.감상;
import static dev.tripdraw.test.fixture.TripFixture.여행;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.timeout;

import dev.tripdraw.post.domain.PostCreateEvent;
import dev.tripdraw.post.domain.PostRepository;
import dev.tripdraw.trip.domain.TripRepository;
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
class PostCreateEventHandlerIntegrationTest {

    @MockBean
    private RouteImageGenerator routeImageGenerator;

    @MockBean
    private PostRepository postRepository;

    @MockBean
    private TripRepository tripRepository;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Test
    void 감상생성_이벤트를_발생시키면_이미지를_생성_요청을_한다() {
        // given
        PostCreateEvent postCreateEvent = new PostCreateEvent(1L, 1L);
        given(tripRepository.getTripWithPoints(postCreateEvent.tripId())).willReturn(여행());
        given(postRepository.getPostWithPointAndMemberById(postCreateEvent.postId())).willReturn(감상());

        // when
        transactionTemplate.executeWithoutResult(action -> applicationEventPublisher.publishEvent(postCreateEvent));

        // then
        then(routeImageGenerator)
                .should(timeout(5000).times(1))
                .generate(any(), any(), any(), any());
    }
}
