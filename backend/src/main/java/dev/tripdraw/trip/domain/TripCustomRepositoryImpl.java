package dev.tripdraw.trip.domain;

import static dev.tripdraw.post.domain.QPost.post;
import static dev.tripdraw.trip.domain.QPoint.point;
import static dev.tripdraw.trip.domain.QTrip.trip;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import dev.tripdraw.common.domain.Paging;
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
    public List<Trip> findAllByConditions(SearchConditions searchConditions, Paging paging) {
        return query
                .selectFrom(trip)
                .join(post).on(trip.id.eq(post.tripId))
                .join(point).on(post.point.id.eq(point.id))
                .where(
                        tripIdLt(paging.lastViewedId()),
                        yearIn(searchConditions.years()),
                        monthIn(searchConditions.months()),
                        dayOfWeekIn(searchConditions.daysOfWeek()),
                        addressLike(searchConditions.address())
                )
                .orderBy(trip.id.desc())
                .limit(paging.limit().longValue() + 1L)
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
}
