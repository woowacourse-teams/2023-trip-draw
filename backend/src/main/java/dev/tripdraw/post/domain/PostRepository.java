package dev.tripdraw.post.domain;

import static dev.tripdraw.post.exception.PostExceptionType.POST_NOT_FOUND;

import dev.tripdraw.post.exception.PostException;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM Post p "
            + "JOIN FETCH p.point "
            + "JOIN FETCH p.member "
            + "where p.tripId = :tripId ")
    List<Post> findAllPostWithPointAndMemberByTripId(@Param("tripId") Long tripId);

    @Query("SELECT p FROM Post p "
            + "JOIN FETCH p.point "
            + "JOIN FETCH p.member m "
            + "where p.id = :postId ")
    Optional<Post> findPostWithPointAndMemberById(@Param("postId") Long postId);

    default Post getPostWithPointAndMemberById(Long id) {
        return findPostWithPointAndMemberById(id)
                .orElseThrow(() -> new PostException(POST_NOT_FOUND));
    }

    @Query("SELECT p FROM Post p "
            + "JOIN FETCH p.point "
            + "JOIN FETCH p.member "
            + "where p.point.id = :pointId")
    Optional<Post> findPostWithPointAndMemberByPointId(@Param("pointId") Long pointId);

    default Post getPostWithPointAndMemberByPointId(Long pointId) {
        return findPostWithPointAndMemberByPointId(pointId)
                .orElseThrow(() -> new PostException(POST_NOT_FOUND));
    }

    @Modifying
    @Query("DELETE FROM Post p WHERE p.member.id = :memberId")
    void deleteByMemberId(@Param("memberId") Long memberId);

    @Modifying
    @Query("DELETE FROM Post p WHERE p.tripId = :tripId")
    void deleteByTripId(@Param("tripId") Long tripId);
}
