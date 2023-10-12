package dev.tripdraw.post.application;

import dev.tripdraw.post.domain.Post;
import dev.tripdraw.post.dto.PostPaging;
import dev.tripdraw.post.dto.PostSearchConditions;
import dev.tripdraw.post.query.PostCustomRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostQueryService {

    private final PostCustomRepository postCustomRepository;

    public PostQueryService(PostCustomRepository postCustomRepository) {
        this.postCustomRepository = postCustomRepository;
    }

    @Transactional(readOnly = true)
    public List<Post> readAllByConditions(PostSearchConditions conditions, PostPaging paging) {
        return postCustomRepository.findAllByConditions(conditions, paging);
    }
}
