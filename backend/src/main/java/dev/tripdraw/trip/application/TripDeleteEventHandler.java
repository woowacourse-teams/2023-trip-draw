package dev.tripdraw.trip.application;

import dev.tripdraw.member.domain.MemberDeleteEvent;
import dev.tripdraw.trip.domain.TripRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class TripDeleteEventHandler {

    private final TripRepository tripRepository;

    public TripDeleteEventHandler(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    @EventListener
    public void deletePostByMemberId(MemberDeleteEvent event) {
        tripRepository.deleteByMemberId(event.memberId());
    }
}
