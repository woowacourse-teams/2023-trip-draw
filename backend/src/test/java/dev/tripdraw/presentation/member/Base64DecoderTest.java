package dev.tripdraw.presentation.member;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class Base64DecoderTest {

    @Test
    void base64로_암호화된_문자열을_해독한다() {
        // given
        Base64Decoder base64Decoder = new Base64Decoder();
        String encoded = "7Ya17ZuE7LaU";

        // when
        String decoded = base64Decoder.decode(encoded);

        // then
        assertThat(decoded).isEqualTo("통후추");
    }
}
