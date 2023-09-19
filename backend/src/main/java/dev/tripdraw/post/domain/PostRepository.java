package dev.tripdraw.post.domain;

import static dev.tripdraw.post.exception.PostExceptionType.POST_NOT_FOUND;

import dev.tripdraw.post.exception.PostException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByTripId(Long tripId);

    default Post getByPostId(Long id) {
        return findById(id)
                .orElseThrow(() -> new PostException(POST_NOT_FOUND));
    }

    void deleteByMemberId(Long memberId);
}
