package dev.tripdraw.post.domain;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import dev.tripdraw.common.domain.Paging;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class PostCustomRepositoryImpl implements PostCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Post> findAllByConditions(SearchConditions conditions, Paging paging) {
        QPost post = QPost.post;

        // TODO: 2023/09/16 연령대, 성별 추가
        return jpaQueryFactory.selectFrom(post)
                .where(
                        ltLastViewedId(post, paging.lastViewedId()),
                        contains(post.point.recordedAt.year(), conditions.years()),
                        contains(post.point.recordedAt.month(), conditions.months()),
                        contains(post.point.recordedAt.dayOfWeek(), conditions.daysOfWeek()),
                        contains(post.point.recordedAt.hour(), conditions.hours()),
                        eqAdress(post.address, conditions.address())
                )
                .limit(paging.limit())
                .orderBy(post.id.desc())
                .fetch();
    }

    private BooleanExpression ltLastViewedId(QPost post, Long lastViewedId) {
        if (lastViewedId == null) {
            return null;
        }

        return post.id.lt(lastViewedId);
    }

    private BooleanExpression contains(NumberExpression<Integer> value, List<Integer> values) {
        if (values == null || values.isEmpty()) {
            return null;
        }

        return value.in(values);
    }

    private BooleanExpression eqAdress(StringPath address, String targetAddress) {
        if (targetAddress == null || targetAddress.isEmpty()) {
            return null;
        }

        return address.eq(targetAddress);
    }
}
