package dev.tripdraw.draw.application;

import static org.springframework.transaction.event.TransactionPhase.AFTER_COMMIT;

import dev.tripdraw.post.domain.Post;
import dev.tripdraw.post.domain.PostCreateEvent;
import dev.tripdraw.post.domain.PostRepository;
import dev.tripdraw.trip.domain.Trip;
import dev.tripdraw.trip.domain.TripRepository;
import java.util.List;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class PostCreateEventHandler {

    private final RouteImageGenerator routeImageGenerator;
    private final PostRepository postRepository;
    private final TripRepository tripRepository;

    public PostCreateEventHandler(
            RouteImageGenerator routeImageGenerator,
            PostRepository postRepository,
            TripRepository tripRepository
    ) {
        this.routeImageGenerator = routeImageGenerator;
        this.postRepository = postRepository;
        this.tripRepository = tripRepository;
    }

    @Async
    @TransactionalEventListener(phase = AFTER_COMMIT)
    public void handle(PostCreateEvent postCreateEvent) {
        Trip trip = tripRepository.getTripWithPoints(postCreateEvent.tripId());
        Post post = postRepository.getByPostId(postCreateEvent.postId());

        String imageUrl = routeImageGenerator.generate(
                trip.getLatitudes(),
                trip.getLongitudes(),
                List.of(post.point().latitude()),
                List.of(post.point().longitude())
        );

        post.changeRouteImageUrl(imageUrl);
        postRepository.save(post);
    }
}
