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
    List<Post> findAllByTripId(@Param("tripId") Long tripId);

    @Query("SELECT p FROM Post p "
            + "JOIN FETCH p.point "
            + "JOIN FETCH p.member m "
            + "where p.id = :postId ")
    Optional<Post> findByPostId(@Param("postId") Long postId);

    default Post getByPostId(Long id) {
        return findByPostId(id)
                .orElseThrow(() -> new PostException(POST_NOT_FOUND));
    }

    default Post getByPointId(Long pointId) {
        return findByPointId(pointId)
                .orElseThrow(() -> new PostException(POST_NOT_FOUND));
    }

    @Query("SELECT p FROM Post p "
            + "JOIN FETCH p.point "
            + "JOIN FETCH p.member "
            + "where p.point.id = :pointId")
    Optional<Post> findByPointId(@Param("pointId") Long pointId);

    @Modifying
    @Query("DELETE FROM Post p WHERE p.member.id = :memberId")
    void deleteByMemberId(@Param("memberId") Long memberId);

    @Modifying
    @Query("DELETE FROM Post p WHERE p.tripId = :tripId")
    void deleteByTripId(@Param("tripId") Long tripId);
}
