package dev.tripdraw.post.domain;

import static dev.tripdraw.post.exception.PostExceptionType.POST_NOT_FOUND;

import dev.tripdraw.post.domain.query.PostCustomRepository;
import dev.tripdraw.post.exception.PostException;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long>, PostCustomRepository {

    List<Post> findAllByTripId(Long tripId);

    @NonNull
    default Post getById(@NonNull Long id) {
        return findById(id)
                .orElseThrow(() -> new PostException(POST_NOT_FOUND));
    }

    void deleteByMemberId(Long memberId);
}
