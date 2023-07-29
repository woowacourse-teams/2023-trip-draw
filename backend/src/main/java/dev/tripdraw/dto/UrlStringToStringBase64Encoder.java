package dev.tripdraw.dto;

import java.util.Base64;

public class UrlStringToStringBase64Encoder {

    public static String encode(String url) {
        if (url == null) {
            return null;
        }

        return Base64.getEncoder().encodeToString(url.getBytes());
    }
}
