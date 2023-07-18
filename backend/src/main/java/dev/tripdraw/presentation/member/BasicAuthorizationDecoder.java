package dev.tripdraw.presentation.member;

import org.apache.tomcat.util.codec.binary.Base64;

public class BasicAuthorizationDecoder {

    private static final int AUTHORIZATION = 0;
    private static final int NICKNAME = 1;
    private static final String AUTH_TYPE = "basic";


    public static String decode(String authorization) {
        String[] authHeader = authorization.split(" ");
        if (!authHeader[AUTHORIZATION].equalsIgnoreCase(AUTH_TYPE)) {
            return null;
        }

        byte[] decodedBytes = Base64.decodeBase64(authHeader[NICKNAME]);
        String decodedString = new String(decodedBytes);

        String[] credentials = decodedString.split(":");
        return credentials[0];
    }
}
