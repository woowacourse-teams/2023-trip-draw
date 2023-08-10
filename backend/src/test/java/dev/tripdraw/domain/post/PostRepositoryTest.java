package dev.tripdraw.domain.post;

import static dev.tripdraw.exception.post.PostExceptionType.POST_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.tripdraw.exception.post.PostException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DataJpaTest
class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Test
    void 입력받은_식별자에_대한_감상이_존재하지_않는다면_예외를_던진다() {
        // expect
        assertThatThrownBy(() -> postRepository.getById(1L))
                .isInstanceOf(PostException.class)
                .hasMessage(POST_NOT_FOUND.getMessage());
    }
}
