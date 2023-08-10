package dev.tripdraw.domain.post;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByTripId(Long tripId);

    void deleteByMemberId(Long memberId);
}
