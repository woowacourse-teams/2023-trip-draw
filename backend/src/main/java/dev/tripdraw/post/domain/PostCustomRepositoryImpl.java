package dev.tripdraw.post.domain;

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

        // 연도, 월, 요일, 시각, 연령대, 성별, 주소
        return jpaQueryFactory.selectFrom(post)
                .where(
                        post.id.lt(paging.lastViewedId()),
                        post.point.recordedAt.year().in(conditions.years()),
                        post.point.recordedAt.month().in(conditions.months()),
                        post.point.recordedAt.dayOfWeek().in(conditions.daysOfWeek()),
                        post.point.recordedAt.hour().in(conditions.hours()),
                        post.address.eq(conditions.address())
                )
                .limit(paging.limit())
                .orderBy(post.id.desc())
                .fetch();
    }
}
