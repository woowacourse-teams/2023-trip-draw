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

    @Query("SELECT p FROM Post p JOIN FETCH p.point where p.tripId = :tripId")
    List<Post> findAllByTripId(@Param("tripId") Long tripId);

    default Post getByPostId(Long id) {
        return findById(id)
                .orElseThrow(() -> new PostException(POST_NOT_FOUND));
    }

    Optional<Post> findByPointId(Long pointId);

    default Post getByPointId(Long pointId) {
        return findByPointId(pointId)
                .orElseThrow(() -> new PostException(POST_NOT_FOUND));
    }

    @Modifying
    @Query("DELETE FROM Post p WHERE p.member.id = :memberId")
    void deleteByMemberId(@Param("memberId") Long memberId);

    @Modifying
    @Query("DELETE FROM Post p WHERE p.tripId = :tripId")
    void deleteByTripId(@Param("tripId") Long tripId);
}
