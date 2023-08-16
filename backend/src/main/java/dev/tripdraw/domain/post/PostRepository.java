package dev.tripdraw.domain.post;

import static dev.tripdraw.exception.post.PostExceptionType.POST_NOT_FOUND;

import dev.tripdraw.exception.post.PostException;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByTripId(Long tripId);

    default Post getById(Long id) {
        return findById(id)
                .orElseThrow(() -> new PostException(POST_NOT_FOUND));
    }

    void deleteByMemberId(Long memberId);
}
