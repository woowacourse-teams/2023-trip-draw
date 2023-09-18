package dev.tripdraw.draw.application;

import static dev.tripdraw.test.TestFixture.감상;
import static dev.tripdraw.test.TestFixture.여행;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import dev.tripdraw.post.domain.PostCreateEvent;
import dev.tripdraw.post.domain.PostRepository;
import dev.tripdraw.trip.domain.TripRepository;
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
class PostCreateEventHandlerTest {

    @Mock
    private RouteImageGenerator routeImageGenerator;

    @Mock
    private PostRepository postRepository;

    @Mock
    private TripRepository tripRepository;

    @InjectMocks
    private PostCreateEventHandler postCreateEventHandler;

    @Test
    void 감상생성_이벤트를_받아_이미지를_생성_요청을_한다() {
        // given
        PostCreateEvent postCreateEvent = new PostCreateEvent(1L, 1L);
        given(tripRepository.getTripWithPoints(postCreateEvent.tripId()))
                .willReturn(여행());
        given(postRepository.getByPostId(postCreateEvent.postId()))
                .willReturn(감상());

        // when
        postCreateEventHandler.handle(postCreateEvent);

        // then
        then(routeImageGenerator)
                .should(times(1))
                .generate(any(), any(), any(), any());
    }
}
