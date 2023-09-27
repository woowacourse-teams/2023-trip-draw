package dev.tripdraw.trip.query;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import dev.tripdraw.trip.domain.Trip;
import dev.tripdraw.trip.dto.TripSearchConditions;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;

import static dev.tripdraw.post.domain.QPost.post;
import static dev.tripdraw.trip.domain.QPoint.point;
import static dev.tripdraw.trip.domain.QTrip.trip;

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
                .where(
                        tripIdLt(tripPaging.lastViewedId()),
                        tripIdInMetAddressCondition(tripSearchConditions)
                )
                .orderBy(trip.id.desc())
                .limit(tripPaging.limit().longValue() + 1L)
                .fetch();
    }

    private BooleanExpression tripIdInMetAddressCondition(TripSearchConditions tripSearchConditions) {
        return trip.id.in(
                JPAExpressions.selectDistinct(point.trip.id)
                        .from(post)
                        .join(point).on(post.point.id.eq(point.id))
                        .where(
                                addressLike(tripSearchConditions.address())
                        )
        );
    }

    private List<Trip> findByTimeConditions(TripSearchConditions tripSearchConditions, TripPaging tripPaging) {
        return query
                .selectFrom(trip)
                .where(
                        tripIdLt(tripPaging.lastViewedId()),
                        tripIdInMetTimeConditions(tripSearchConditions)
                )
                .orderBy(trip.id.desc())
                .limit(tripPaging.limit().longValue() + 1L)
                .fetch();
    }

    private BooleanExpression tripIdInMetTimeConditions(TripSearchConditions tripSearchConditions) {
        return trip.id.in(
                JPAExpressions.selectDistinct(point.trip.id)
                        .from(point)
                        .where(
                                yearIn(tripSearchConditions.years()),
                                monthIn(tripSearchConditions.months()),
                                dayOfWeekIn(tripSearchConditions.daysOfWeek()),
                                hasPost()
                        )
        );
    }

    private BooleanExpression hasPost() {
        return point.hasPost.isTrue();
    }

    private List<Trip> findWithoutCondition(TripPaging tripPaging) {
        return query
                .selectFrom(trip)
                .where(
                        tripIdLt(tripPaging.lastViewedId()),
                        tripIdInHasPost()
                )
                .orderBy(trip.id.desc())
                .limit(tripPaging.limit().longValue() + 1L)
                .fetch();
    }

    private BooleanExpression tripIdInHasPost() {
        return trip.id.in(
                JPAExpressions.selectDistinct(point.trip.id)
                        .from(post)
                        .join(point).on(post.point.id.eq(point.id))
                        .where(hasPost())
        );
    }
}
