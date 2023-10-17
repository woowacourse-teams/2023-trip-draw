package dev.tripdraw.post.domain;

import static dev.tripdraw.test.fixture.MemberFixture.사용자;
import static dev.tripdraw.test.fixture.PointFixture.새로운_위치정보;
import static dev.tripdraw.test.fixture.PostFixture.새로운_감상;
import static org.assertj.core.api.Assertions.assertThat;

import dev.tripdraw.common.config.JpaConfig;
import dev.tripdraw.common.config.QueryDslConfig;
import dev.tripdraw.member.domain.Member;
import dev.tripdraw.member.domain.MemberRepository;
import dev.tripdraw.post.dto.PostPaging;
import dev.tripdraw.post.dto.PostSearchConditions;
import dev.tripdraw.trip.domain.Point;
import dev.tripdraw.trip.domain.PointRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Transactional
@Import({JpaConfig.class, QueryDslConfig.class})
@SpringBootTest
class PostDynamicQueryRepositoryTest {

    @Autowired
    private PostDynamicQueryRepository postDynamicQueryRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PointRepository pointRepository;

    @Test
    void 조건에_해당하는_감상을_조회한다() {
        // given
        Member member = memberRepository.save(사용자());
        Point mayPoint = pointRepository.save(새로운_위치정보(2023, 5, 12, 15, 30));
        Point julyPoint1 = pointRepository.save(새로운_위치정보(2023, 7, 12, 15, 30));
        Point julyPoint2 = pointRepository.save(새로운_위치정보(2023, 7, 12, 15, 30));
        Post jejuMayPost = postRepository.save(새로운_감상(mayPoint, member.id(), "제주특별자치도 제주시 애월읍"));
        Post jejuJulyPost = postRepository.save(새로운_감상(julyPoint1, member.id(), "제주특별자치도 제주시 애월읍"));
        Post seoulJulyPost = postRepository.save(새로운_감상(julyPoint2, member.id(), "서울특별시 송파구 문정동"));

        PostSearchConditions conditions = PostSearchConditions.builder()
                .months(Set.of(7))
                .build();
        PostPaging paging = new PostPaging(null, 10);

        // when
        List<Post> posts = postDynamicQueryRepository.findAllByConditions(conditions, paging);

        // then
        assertThat(posts).containsExactly(seoulJulyPost, jejuJulyPost);
    }

    @Test
    void 감상을_전체_조회한다() {
        // given
        Member member = memberRepository.save(사용자());
        for (int i = 1; i <= 5; i++) {
            Point point = pointRepository.save(새로운_위치정보(LocalDateTime.now()));
            postRepository.save(새로운_감상(point, member.id(), "주소 " + i));
        }
        PostPaging postPaging = new PostPaging(null, 3);

        // when
        List<Post> posts = postDynamicQueryRepository.findAll(postPaging);

        // then
        assertThat(posts)
                .extracting(Post::address)
                .containsExactly("주소 5", "주소 4", "주소 3", "주소 2");
    }
}

