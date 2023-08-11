package dev.tripdraw.application.draw;

import static dev.tripdraw.test.TestFixture.감상;
import static dev.tripdraw.test.TestFixture.여행;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import dev.tripdraw.domain.post.PostCreateEvent;
import dev.tripdraw.domain.post.PostRepository;
import dev.tripdraw.domain.trip.TripRepository;
import dev.tripdraw.test.TestSyncConfig;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.support.TransactionTemplate;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ContextConfiguration(classes = TestSyncConfig.class)
@SpringBootTest
public class PostCreateEventHandlerIntegrationTest {

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
        given(tripRepository.getTripWithPoints(postCreateEvent.tripId()))
                .willReturn(여행());
        given(postRepository.getById(postCreateEvent.postId()))
                .willReturn(감상());

        // when
        transactionTemplate.executeWithoutResult(action -> {
            applicationEventPublisher.publishEvent(postCreateEvent);
        });

        // then
        then(routeImageGenerator)
                .should(times(1))
                .generate(any(), any(), any(), any());
    }
}
