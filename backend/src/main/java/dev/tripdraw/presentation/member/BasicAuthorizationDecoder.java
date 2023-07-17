package dev.tripdraw.presentation.member;

import org.apache.tomcat.util.codec.binary.Base64;

public class BasicAuthorizationDecoder {

    public static String decode(String authorization) {
        String[] authHeader = authorization.split(" ");
        if (!authHeader[0].equalsIgnoreCase("basic")) {
            return null;
        }

        byte[] decodedBytes = Base64.decodeBase64(authHeader[1]);
        String decodedString = new String(decodedBytes);

        String[] credentials = decodedString.split(":");
        return credentials[0];
    }
}
