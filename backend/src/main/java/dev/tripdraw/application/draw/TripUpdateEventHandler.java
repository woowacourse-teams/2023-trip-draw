package dev.tripdraw.application.draw;

import dev.tripdraw.domain.trip.Trip;
import dev.tripdraw.domain.trip.TripRepository;
import dev.tripdraw.domain.trip.TripUpdateEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.support.TransactionTemplate;

@Component
public class TripUpdateEventHandler {

    private final RouteImageGenerator routeImageGenerator;
    private final TripRepository tripRepository;
    private final TransactionTemplate transactionTemplate;

    public TripUpdateEventHandler(
            RouteImageGenerator routeImageGenerator,
            TripRepository tripRepository,
            TransactionTemplate transactionTemplate
    ) {
        this.routeImageGenerator = routeImageGenerator;
        this.tripRepository = tripRepository;
        this.transactionTemplate = transactionTemplate;
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(TripUpdateEvent tripUpdateEvent) {
        Trip trip = tripRepository.getTripWithPoints(tripUpdateEvent.tripId());

        String imageUrl = routeImageGenerator.generate(
                trip.getLatitudes(),
                trip.getLongitudes(),
                trip.getPointedLatitudes(),
                trip.getPointedLongitudes()
        );

        transactionTemplate.executeWithoutResult(action -> {
            trip.changeRouteImageUrl(imageUrl);
            tripRepository.save(trip);
        });
    }
}
