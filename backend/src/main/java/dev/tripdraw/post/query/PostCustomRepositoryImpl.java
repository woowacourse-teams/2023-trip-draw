package dev.tripdraw.post.query;

import static dev.tripdraw.post.domain.QPost.post;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import dev.tripdraw.post.domain.Post;
import dev.tripdraw.post.dto.PostSearchConditions;
import dev.tripdraw.post.dto.PostSearchPaging;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Repository
public class PostCustomRepositoryImpl implements PostCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Post> findAllByConditions(PostSearchConditions conditions, PostSearchPaging paging) {
        // TODO: 2023/09/16 연령대, 성별 추가
        return jpaQueryFactory.selectFrom(post)
                .leftJoin(post.point).fetchJoin()
                .where(
                        postIdLt(paging.lastViewedId()),
                        yearIn(conditions.years()),
                        monthIn(conditions.months()),
                        dayOfWeekIn(conditions.daysOfWeek()),
                        hourIn(conditions.hours()),
                        addressLike(conditions.address())
                )
                .limit(paging.limit().longValue() + 1L)
                .orderBy(post.id.desc())
                .fetch();
    }

    private BooleanExpression postIdLt(Long lastViewedId) {
        if (lastViewedId == null) {
            return null;
        }

        return post.id.lt(lastViewedId);
    }

    private BooleanExpression yearIn(Set<Integer> years) {
        if (years == null || years.isEmpty()) {
            return null;
        }

        return post.point.recordedAt.year().in(years);
    }

    private BooleanExpression monthIn(Set<Integer> months) {
        if (months == null || months.isEmpty()) {
            return null;
        }

        return post.point.recordedAt.month().in(months);
    }

    private BooleanExpression dayOfWeekIn(Set<Integer> daysOfWeek) {
        if (daysOfWeek == null || daysOfWeek.isEmpty()) {
            return null;
        }

        return post.point.recordedAt.dayOfWeek().in(daysOfWeek);
    }

    private BooleanExpression hourIn(Set<Integer> hours) {
        if (hours == null || hours.isEmpty()) {
            return null;
        }

        return post.point.recordedAt.hour().in(hours);
    }

    private BooleanExpression addressLike(String address) {
        if (address == null || address.isEmpty()) {
            return null;
        }

        return post.address.like(address + "%");
    }
}
