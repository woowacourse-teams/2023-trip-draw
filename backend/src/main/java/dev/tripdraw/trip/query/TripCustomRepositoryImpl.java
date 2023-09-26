package dev.tripdraw.trip.query;

import static dev.tripdraw.post.domain.QPost.post;
import static dev.tripdraw.trip.domain.QPoint.point;
import static dev.tripdraw.trip.domain.QTrip.trip;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import dev.tripdraw.trip.domain.Trip;
import dev.tripdraw.trip.dto.TripSearchConditions;
import io.micrometer.common.util.StringUtils;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

@RequiredArgsConstructor
@Repository
public class TripCustomRepositoryImpl implements TripCustomRepository {

    private final JPAQueryFactory query;

    @Override
    public List<Trip> findAllByConditions(TripSearchConditions tripSearchConditions, TripPaging tripPaging) {
        if (tripSearchConditions.hasAllConditions()) {
            return findByAllConditions(tripSearchConditions, tripPaging);
        }

        if (tripSearchConditions.hasAddressCondition()) {
            return findByAddressCondition(tripSearchConditions, tripPaging);
        }

        if (tripSearchConditions.hasTimeConditions()) {
            return findByTimeConditions(tripSearchConditions, tripPaging);
        }

        return findWithoutCondition(tripPaging);
    }

    private List<Trip> findByAllConditions(TripSearchConditions tripSearchConditions, TripPaging tripPaging) {
        return query
                .selectFrom(trip)
                .distinct()
                .join(post).on(trip.id.eq(post.tripId))
                .join(point).on(post.point.id.eq(point.id))
                .where(
                        tripIdLt(tripPaging.lastViewedId()),
                        yearIn(tripSearchConditions.years()),
                        monthIn(tripSearchConditions.months()),
                        dayOfWeekIn(tripSearchConditions.daysOfWeek()),
                        addressLike(tripSearchConditions.address())
                )
                .orderBy(trip.id.desc())
                .limit(tripPaging.limit().longValue() + 1L)
                .fetch();
    }

    private BooleanExpression tripIdLt(Long lastViewedId) {
        if (lastViewedId == null) {
            return null;
        }
        return trip.id.lt(lastViewedId);
    }

    private BooleanExpression yearIn(Set<Integer> years) {
        if (CollectionUtils.isEmpty(years)) {
            return null;
        }
        return point.recordedAt.year().in(years);
    }

    private BooleanExpression monthIn(Set<Integer> months) {
        if (CollectionUtils.isEmpty(months)) {
            return null;
        }
        return point.recordedAt.month().in(months);
    }

    private BooleanExpression dayOfWeekIn(Set<Integer> daysOfWeek) {
        if (CollectionUtils.isEmpty(daysOfWeek)) {
            return null;
        }
        return point.recordedAt.dayOfWeek().in(daysOfWeek);
    }

    private BooleanExpression addressLike(String address) {
        if (StringUtils.isBlank(address)) {
            return null;
        }
        return post.address.like(address + "%");
    }

    private List<Trip> findByAddressCondition(TripSearchConditions tripSearchConditions, TripPaging tripPaging) {
        return query
                .selectFrom(trip)
                .distinct()
                .join(post).on(trip.id.eq(post.point.trip.id))
                .where(
                        addressLike(tripSearchConditions.address())
                )
                .orderBy(trip.id.desc())
                .limit(tripPaging.limit().longValue() + 1L)
                .fetch();
    }

    private List<Trip> findByTimeConditions(TripSearchConditions tripSearchConditions, TripPaging tripPaging) {
        return query
                .selectFrom(trip)
                .distinct()
                .join(point).on(point.trip.id.eq(trip.id))
                .where(
                        tripIdLt(tripPaging.lastViewedId()),
                        yearIn(tripSearchConditions.years()),
                        monthIn(tripSearchConditions.months()),
                        dayOfWeekIn(tripSearchConditions.daysOfWeek())
                )
                .orderBy(trip.id.desc())
                .limit(tripPaging.limit().longValue() + 1L)
                .fetch();
    }

    private List<Trip> findWithoutCondition(TripPaging tripPaging) {
        return query
                .selectFrom(trip)
                .distinct()
                .where(tripIdLt(tripPaging.lastViewedId()))
                .orderBy(trip.id.desc())
                .limit(tripPaging.limit().longValue() + 1L)
                .fetch();
    }
}
