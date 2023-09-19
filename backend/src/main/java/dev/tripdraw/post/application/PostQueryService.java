package dev.tripdraw.post.application;

import dev.tripdraw.post.domain.Post;
import dev.tripdraw.post.dto.PostSearchConditions;
import dev.tripdraw.post.dto.PostSearchPaging;
import dev.tripdraw.post.query.PostCustomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostQueryService {

    private final PostCustomRepository postCustomRepository;

    public PostQueryService(PostCustomRepository postCustomRepository) {
        this.postCustomRepository = postCustomRepository;
    }

    @Transactional(readOnly = true)
    public List<Post> findAllByConditions(PostSearchConditions conditions, PostSearchPaging paging) {
        return postCustomRepository.findAllByConditions(conditions, paging);
    }
}
