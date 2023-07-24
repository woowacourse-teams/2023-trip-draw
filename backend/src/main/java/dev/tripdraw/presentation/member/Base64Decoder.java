package dev.tripdraw.presentation.member;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Component;

@Component
public class Base64Decoder {

    public String decode(String encoded) {
        byte[] decodedBytes = Base64.decodeBase64(encoded);
        return new String(decodedBytes);
    }
}
