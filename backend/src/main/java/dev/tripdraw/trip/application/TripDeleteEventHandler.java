package dev.tripdraw.trip.application;

import dev.tripdraw.member.domain.MemberDeleteEvent;
import dev.tripdraw.trip.domain.PointRepository;
import dev.tripdraw.trip.domain.TripRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TripDeleteEventHandler {

    private final TripRepository tripRepository;
    private final PointRepository pointRepository;

    @EventListener
    public void deletePostByMemberId(MemberDeleteEvent event) {
        List<Long> tripIds = tripRepository.findAllTripIdsByMemberId(event.memberId());
        pointRepository.deleteByTripIds(tripIds);
        tripRepository.deleteByMemberId(event.memberId());
    }
}
