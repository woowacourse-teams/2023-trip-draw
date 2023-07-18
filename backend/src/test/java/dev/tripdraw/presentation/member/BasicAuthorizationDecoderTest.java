package dev.tripdraw.presentation.member;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class BasicAuthorizationDecoderTest {

    @Test
    void base64로_암호화된_문자열을_해독한다() {
        //given
        String encoded = "BASIC 7Ya17ZuE7LaU";

        //when
        String decoded = BasicAuthorizationDecoder.decode(encoded);

        //then
        Assertions.assertThat(decoded).isEqualTo("통후추");
    }
}
