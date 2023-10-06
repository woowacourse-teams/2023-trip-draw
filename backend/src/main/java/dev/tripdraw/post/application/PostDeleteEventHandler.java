package dev.tripdraw.post.application;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

import dev.tripdraw.member.domain.MemberDeleteEvent;
import dev.tripdraw.post.domain.PostRepository;
import dev.tripdraw.trip.domain.TripDeleteEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
public class PostDeleteEventHandler {

    private final PostRepository postRepository;

    public PostDeleteEventHandler(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Order(HIGHEST_PRECEDENCE)
    @EventListener
    public void deletePostByMemberId(MemberDeleteEvent event) {
        postRepository.deleteByMemberId(event.memberId());
    }

    @EventListener
    public void deletePostByTripId(TripDeleteEvent event) {
        postRepository.deleteByTripId(event.tripId());
    }
}
