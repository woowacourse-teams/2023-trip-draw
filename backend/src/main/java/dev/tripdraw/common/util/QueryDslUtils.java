package dev.tripdraw.common.util;

import static dev.tripdraw.post.domain.QPost.post;
import static dev.tripdraw.trip.domain.QPoint.point;
import static dev.tripdraw.trip.domain.QTrip.trip;
import static org.springframework.util.CollectionUtils.isEmpty;

import com.querydsl.core.types.dsl.BooleanExpression;
import io.micrometer.common.util.StringUtils;
import java.util.Set;

public abstract class QueryDslUtils {

    private QueryDslUtils() {
    }

    public static BooleanExpression tripIdLt(Long lastViewedId) {
        if (lastViewedId == null) {
            return null;
        }
        return trip.id.lt(lastViewedId);
    }

    public static BooleanExpression postIdLt(Long lastViewedId) {
        if (lastViewedId == null) {
            return null;
        }
        return post.id.lt(lastViewedId);
    }

    public static BooleanExpression pointTripIdLt(Long lastViewedId) {
        if (lastViewedId == null) {
            return null;
        }
        return point.trip.id.lt(lastViewedId);
    }

    public static BooleanExpression postTripIdLt(Long lastViewedId) {
        if (lastViewedId == null) {
            return null;
        }
        return post.tripId.lt(lastViewedId);
    }

    public static BooleanExpression yearIn(Set<Integer> years) {
        if (isEmpty(years)) {
            return null;
        }
        return point.recordedAt.year().in(years);
    }

    public static BooleanExpression monthIn(Set<Integer> months) {
        if (isEmpty(months)) {
            return null;
        }
        return point.recordedAt.month().in(months);
    }

    public static BooleanExpression dayOfWeekIn(Set<Integer> daysOfWeek) {
        if (isEmpty(daysOfWeek)) {
            return null;
        }
        return point.recordedAt.dayOfWeek().in(daysOfWeek);
    }

    public static BooleanExpression hourIn(Set<Integer> hours) {
        if (isEmpty(hours)) {
            return null;
        }
        return point.recordedAt.hour().in(hours);
    }

    public static BooleanExpression addressLike(String address) {
        if (StringUtils.isBlank(address)) {
            return null;
        }
        return post.address.like(address + "%");
    }
}
