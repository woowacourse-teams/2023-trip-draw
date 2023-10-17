package dev.tripdraw.trip.domain;

import static dev.tripdraw.common.util.QueryDslUtils.addressLike;
import static dev.tripdraw.common.util.QueryDslUtils.dayOfWeekIn;
import static dev.tripdraw.common.util.QueryDslUtils.monthIn;
import static dev.tripdraw.common.util.QueryDslUtils.pointTripIdLt;
import static dev.tripdraw.common.util.QueryDslUtils.postTripIdLt;
import static dev.tripdraw.common.util.QueryDslUtils.tripIdLt;
import static dev.tripdraw.common.util.QueryDslUtils.yearIn;
import static dev.tripdraw.post.domain.QPost.post;
import static dev.tripdraw.trip.domain.QPoint.point;
import static dev.tripdraw.trip.domain.QTrip.trip;

import com.querydsl.jpa.impl.JPAQueryFactory;
import dev.tripdraw.trip.dto.TripPaging;
import dev.tripdraw.trip.dto.TripSearchConditions;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class TripDynamicQueryRepository {

    private static final long UNIT_FOR_HAS_NEXT_PAGE = 1L;

    private final JPAQueryFactory jpaQueryFactory;

    public List<Trip> findAllByConditions(TripSearchConditions tripSearchConditions, TripPaging paging) {
        if (tripSearchConditions.hasNoCondition()) {
            List<Long> tripIds = getTripIdsWithoutCondition(paging);
            return getTripsIn(tripIds);
        }

        if (tripSearchConditions.hasOnlyAddressCondition()) {
            List<Long> tripIds = getTripIdsWithAddressCondition(tripSearchConditions, paging);
            return getTripsIn(tripIds);
        }

        if (tripSearchConditions.hasOnlyTimeConditions()) {
            List<Long> tripIds = getTripIdsWithTimeConditions(tripSearchConditions, paging);
            return getTripsIn(tripIds);
        }

        List<Long> tripIds = getTripIdsWithAllConditions(tripSearchConditions, paging);
        return getTripsIn(tripIds);
    }

    private List<Long> getTripIdsWithoutCondition(TripPaging paging) {
        return jpaQueryFactory.selectDistinct(point.trip.id).from(point)
                .where(
                        point.hasPost.isTrue(),
                        pointTripIdLt(paging.lastViewedId())
                )
                .orderBy(point.trip.id.desc())
                .limit(paging.limit().longValue() + UNIT_FOR_HAS_NEXT_PAGE)
                .fetch();
    }

    private List<Trip> getTripsIn(List<Long> tripIds) {
        return jpaQueryFactory.selectFrom(trip)
                .innerJoin(trip.member).fetchJoin()
                .where(trip.id.in(tripIds))
                .orderBy(trip.id.desc())
                .fetch();
    }

    private List<Long> getTripIdsWithAddressCondition(TripSearchConditions tripSearchConditions, TripPaging paging) {
        return jpaQueryFactory.selectDistinct(post.tripId).from(post)
                .where(
                        addressLike(tripSearchConditions.address()),
                        postTripIdLt(paging.lastViewedId())
                )
                .orderBy(post.tripId.desc())
                .limit(paging.limit().longValue() + UNIT_FOR_HAS_NEXT_PAGE)
                .fetch();
    }

    private List<Long> getTripIdsWithTimeConditions(TripSearchConditions tripSearchConditions, TripPaging paging) {
        return jpaQueryFactory.selectDistinct(point.trip.id).from(point)
                .where(
                        yearIn(tripSearchConditions.years()),
                        monthIn(tripSearchConditions.months()),
                        dayOfWeekIn(tripSearchConditions.daysOfWeek()),
                        point.hasPost.isTrue(),
                        pointTripIdLt(paging.lastViewedId())
                )
                .orderBy(point.trip.id.desc())
                .limit(paging.limit().longValue() + UNIT_FOR_HAS_NEXT_PAGE)
                .fetch();
    }

    private List<Long> getTripIdsWithAllConditions(TripSearchConditions tripSearchConditions, TripPaging paging) {
        return jpaQueryFactory.selectDistinct(point.trip.id).from(point)
                .innerJoin(post).on(post.point.id.eq(point.id))
                .where(
                        yearIn(tripSearchConditions.years()),
                        monthIn(tripSearchConditions.months()),
                        dayOfWeekIn(tripSearchConditions.daysOfWeek()),
                        addressLike(tripSearchConditions.address()),
                        pointTripIdLt(paging.lastViewedId())
                )
                .orderBy(point.trip.id.desc())
                .limit(paging.limit().longValue() + UNIT_FOR_HAS_NEXT_PAGE)
                .fetch();
    }

    public List<Trip> findAll(TripPaging paging) {
        return jpaQueryFactory
                .selectFrom(trip)
                .where(tripIdLt(paging.lastViewedId()))
                .orderBy(trip.id.desc())
                .limit(paging.limit().longValue() + UNIT_FOR_HAS_NEXT_PAGE)
                .fetch();
    }
}
