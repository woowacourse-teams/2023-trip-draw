package dev.tripdraw.post.query;

import static dev.tripdraw.common.util.QueryDslUtils.addressLike;
import static dev.tripdraw.common.util.QueryDslUtils.dayOfWeekIn;
import static dev.tripdraw.common.util.QueryDslUtils.hourIn;
import static dev.tripdraw.common.util.QueryDslUtils.monthIn;
import static dev.tripdraw.common.util.QueryDslUtils.postIdLt;
import static dev.tripdraw.common.util.QueryDslUtils.yearIn;
import static dev.tripdraw.post.domain.QPost.post;

import com.querydsl.jpa.impl.JPAQueryFactory;
import dev.tripdraw.post.domain.Post;
import dev.tripdraw.post.dto.PostPaging;
import dev.tripdraw.post.dto.PostSearchConditions;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class PostCustomRepositoryImpl implements PostCustomRepository {

    private static final long UNIT_FOR_HAS_NEXT_PAGE = 1L;

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Post> findAllByConditions(PostSearchConditions conditions, PostPaging paging) {
        return jpaQueryFactory.selectFrom(post)
                .innerJoin(post.point).fetchJoin()
                .innerJoin(post.member).fetchJoin()
                .where(
                        yearIn(conditions.years()),
                        monthIn(conditions.months()),
                        dayOfWeekIn(conditions.daysOfWeek()),
                        hourIn(conditions.hours()),
                        addressLike(conditions.address()),
                        postIdLt(paging.lastViewedId())
                )
                .limit(paging.limit().longValue() + UNIT_FOR_HAS_NEXT_PAGE)
                .orderBy(post.id.desc())
                .fetch();
    }

    @Override
    public List<Post> findAll(PostPaging paging) {
        return jpaQueryFactory.selectFrom(post)
                .leftJoin(post.point).fetchJoin()
                .where(postIdLt(paging.lastViewedId()))
                .limit(paging.limit().longValue() + UNIT_FOR_HAS_NEXT_PAGE)
                .orderBy(post.id.desc())
                .fetch();
    }
}
