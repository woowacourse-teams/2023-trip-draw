package dev.tripdraw.application.draw;

import dev.tripdraw.domain.post.Post;
import dev.tripdraw.domain.post.PostCreateEvent;
import dev.tripdraw.domain.post.PostRepository;
import dev.tripdraw.domain.trip.Trip;
import dev.tripdraw.domain.trip.TripRepository;
import java.util.List;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.support.TransactionTemplate;

@Component
public class PostCreateEventHandler {

    private final RouteImageGenerator routeImageGenerator;
    private final PostRepository postRepository;
    private final TripRepository tripRepository;
    private final TransactionTemplate transactionTemplate;

    public PostCreateEventHandler(
            RouteImageGenerator routeImageGenerator,
            PostRepository postRepository,
            TripRepository tripRepository,
            TransactionTemplate transactionTemplate
    ) {
        this.routeImageGenerator = routeImageGenerator;
        this.postRepository = postRepository;
        this.tripRepository = tripRepository;
        this.transactionTemplate = transactionTemplate;
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(PostCreateEvent postCreateEvent) {
        Trip trip = tripRepository.getTripWithPoints(postCreateEvent.tripId());
        Post post = postRepository.getById(postCreateEvent.postId());

        String imageUrl = routeImageGenerator.generate(
                trip.getLatitudes(),
                trip.getLongitudes(),
                List.of(post.point().latitude()),
                List.of(post.point().longitude())
        );

        transactionTemplate.executeWithoutResult(action -> {
            post.changeRouteImageUrl(imageUrl);
            postRepository.save(post);
        });
    }
}
