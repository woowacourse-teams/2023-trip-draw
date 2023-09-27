package dev.tripdraw.post.domain;

import dev.tripdraw.post.exception.PostException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import static dev.tripdraw.post.exception.PostExceptionType.POST_NOT_FOUND;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM Post p JOIN FETCH p.point where p.tripId = :tripId")
    List<Post> findAllByTripId(@Param("tripId") Long tripId);

    default Post getByPostId(Long id) {
        return findById(id)
                .orElseThrow(() -> new PostException(POST_NOT_FOUND));
    }

    @Modifying
    @Query("DELETE FROM Post p WHERE p.memberId = :memberId")
    void deleteByMemberId(@Param(value = "memberId") Long memberId);
}
