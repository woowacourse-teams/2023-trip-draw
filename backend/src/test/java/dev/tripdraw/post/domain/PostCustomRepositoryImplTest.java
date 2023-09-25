package dev.tripdraw.post.domain;

import static dev.tripdraw.common.auth.OauthType.KAKAO;
import static org.assertj.core.api.Assertions.assertThat;

import dev.tripdraw.common.config.JpaConfig;
import dev.tripdraw.common.config.QueryDslConfig;
import dev.tripdraw.member.domain.Member;
import dev.tripdraw.member.domain.MemberRepository;
import dev.tripdraw.post.dto.PostSearchConditions;
import dev.tripdraw.post.dto.PostSearchPaging;
import dev.tripdraw.post.query.PostCustomRepository;
import dev.tripdraw.trip.domain.Point;
import dev.tripdraw.trip.domain.Trip;
import dev.tripdraw.trip.domain.TripRepository;
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
@SpringBootTest
@Import({JpaConfig.class, QueryDslConfig.class})
class PostCustomRepositoryImplTest {

    @Autowired
    private PostCustomRepository postCustomRepository;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostRepository postRepository;

    @Test
    void 조건에_해당하는_감상을_조회한다() {
        // given
        Member member = memberRepository.save(new Member("통후추", "kakaoId", KAKAO));
        Trip trip = Trip.of(member.id(), member.nickname());

        Point firstPoint = new Point(3.14, 5.25, LocalDateTime.of(2023, 5, 1, 17, 30));
        Point secondPoint = new Point(3.14, 5.25, LocalDateTime.of(2023, 5, 3, 18, 30));
        Point thirdPoint = new Point(3.14, 5.25, LocalDateTime.of(2023, 7, 1, 18, 30));

        trip.add(firstPoint);
        trip.add(secondPoint);
        trip.add(thirdPoint);

        tripRepository.save(trip);

        Post firstPost = new Post("제목", firstPoint, "위치", "오늘은 날씨가 좋네요.", member.id(), trip.id());
        Post secondPost = new Post("제목", secondPoint, "위치", "오늘은 날씨가 좋네요.", member.id(), trip.id());
        Post thirdPost = new Post("제목", thirdPoint, "위치", "오늘은 날씨가 좋네요.", member.id(), trip.id());

        postRepository.save(firstPost);
        postRepository.save(secondPost);
        postRepository.save(thirdPost);

        PostSearchConditions firstConditions = PostSearchConditions.builder()
                .months(Set.of(5))
                .build();

        PostSearchConditions secondConditions = PostSearchConditions.builder()
                .hours(Set.of(18))
                .build();

        PostSearchPaging paging = new PostSearchPaging(null, 10);

        // when
        List<Post> firstPosts = postCustomRepository.findAllByConditions(firstConditions, paging);
        List<Post> secondPosts = postCustomRepository.findAllByConditions(secondConditions, paging);

        // then
        assertThat(firstPosts.stream().map(Post::id).toList()).containsExactly(secondPost.id(), firstPost.id());
        assertThat(secondPosts.stream().map(Post::id).toList()).containsExactly(thirdPost.id(), secondPost.id());
    }
}

