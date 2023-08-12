package dev.tripdraw.application.draw;

import static org.springframework.transaction.event.TransactionPhase.AFTER_COMMIT;

import dev.tripdraw.domain.trip.Trip;
import dev.tripdraw.domain.trip.TripRepository;
import dev.tripdraw.domain.trip.TripUpdateEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class TripUpdateEventHandler {

    private final RouteImageGenerator routeImageGenerator;
    private final TripRepository tripRepository;

    public TripUpdateEventHandler(RouteImageGenerator routeImageGenerator, TripRepository tripRepository) {
        this.routeImageGenerator = routeImageGenerator;
        this.tripRepository = tripRepository;
    }

    @Async
    @TransactionalEventListener(phase = AFTER_COMMIT)
    public void handle(TripUpdateEvent tripUpdateEvent) {
        Trip trip = tripRepository.getTripWithPoints(tripUpdateEvent.tripId());

        String imageUrl = routeImageGenerator.generate(
                trip.getLatitudes(),
                trip.getLongitudes(),
                trip.getPointedLatitudes(),
                trip.getPointedLongitudes()
        );

        trip.changeRouteImageUrl(imageUrl);
        tripRepository.save(trip);
    }
}
