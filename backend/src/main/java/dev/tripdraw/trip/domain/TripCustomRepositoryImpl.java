package dev.tripdraw.trip.domain;

import static dev.tripdraw.post.domain.QPost.post;
import static dev.tripdraw.trip.domain.QPoint.point;
import static dev.tripdraw.trip.domain.QTrip.trip;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import dev.tripdraw.common.domain.Paging;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class TripCustomRepositoryImpl implements TripCustomRepository {

    private final JPAQueryFactory query;

    @Override
    public List<Trip> findAllByConditions(SearchConditions searchConditions, Paging paging) {
        return query
                .selectFrom(trip)
                .join(post).on(trip.id.eq(post.tripId))
                .join(point).on(post.point.id.eq(point.id))
                .where(
                        tripsBeforeLastViewedId(paging.lastViewedId()),
                        tripsRecordedAtYears(searchConditions.years()),
                        tripsRecordedAtMonths(searchConditions.months()),
                        tripsRecordedAtDaysOfWeek(searchConditions.daysOfWeek()),
                        tripsAddressed(searchConditions.address())
                )
                .orderBy(trip.id.desc())
                .limit(paging.limit() + 1)
                .fetch();
    }

    private BooleanExpression tripsBeforeLastViewedId(Long lastViewedId) {
        if (lastViewedId == null) {
            return null;
        }
        return trip.id.lt(lastViewedId);
    }

    private BooleanExpression tripsRecordedAtYears(Set<Integer> years) {
        if (years.isEmpty()) {
            return null;
        }
        return point.recordedAt.year().in(years);
    }

    private BooleanExpression tripsRecordedAtMonths(Set<Integer> months) {
        if (months.isEmpty()) {
            return null;
        }
        return point.recordedAt.month().in(months);
    }

    private BooleanExpression tripsRecordedAtDaysOfWeek(Set<Integer> daysOfWeek) {
        if (daysOfWeek.isEmpty()) {
            return null;
        }
        return point.recordedAt.dayOfWeek().in(daysOfWeek);
    }

    private BooleanExpression tripsAddressed(String address) {
        if (address.isEmpty()) {
            return null;
        }
        return post.address.like(address + "%");
    }
}
