package dev.tripdraw.area.application;

import dev.tripdraw.area.domain.Area;
import dev.tripdraw.area.dto.OpenApiAccessTokenResponse;
import dev.tripdraw.area.dto.OpenApiAreaResponse;
import dev.tripdraw.area.dto.OpenApiTotalAreaResponse;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@RequiredArgsConstructor
@Component
public class OpenApiAreaDownloader {

    private static final String AREA_API_DOMAIN = "https://sgisapi.kostat.go.kr";
    private static final String AREA_CODE_NAME = "cd";
    private static final String ACCESS_TOKEN_NAME = "accessToken";
    private static final String SERVICE_KEY_NAME = "consumer_key";
    private static final String SECRET_KEY_NAME = "consumer_secret";
    private static final String AUTHENTICATION_URI = "/OpenAPI3/auth/authentication.json";
    private static final String AREA_URI = "/OpenAPI3/addr/stage.json";

    private final RestTemplate restTemplate;

    @Value("${open.api.area.service}")
    private String serviceSecret;

    @Value("${open.api.area.key}")
    private String keySecret;

    public List<Area> download() {
        String accessToken = requestAccessToken();
        return downloadAreas(accessToken);
    }

    private String requestAccessToken() {
        URI authenticationUri = UriComponentsBuilder.fromUriString(AREA_API_DOMAIN)
                .path(AUTHENTICATION_URI)
                .queryParam(SERVICE_KEY_NAME, serviceSecret)
                .queryParam(SECRET_KEY_NAME, keySecret)
                .encode()
                .build()
                .toUri();

        OpenApiAccessTokenResponse accessTokenResponse = restTemplate.getForObject(
                authenticationUri,
                OpenApiAccessTokenResponse.class
        );

        return accessTokenResponse.result().get(ACCESS_TOKEN_NAME);
    }

    private List<Area> downloadAreas(String accessToken) {
        List<Area> allAreas = new ArrayList<>();

        List<OpenApiAreaResponse> sidoResponses = requestAreas(accessToken, null);
        for (OpenApiAreaResponse sidoResponse : sidoResponses) {
            fetchSigunguAndUmd(accessToken, allAreas, sidoResponse);
        }
        return allAreas;
    }

    private List<OpenApiAreaResponse> requestAreas(String accessToken, String code) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(AREA_API_DOMAIN)
                .path(AREA_URI)
                .queryParam(ACCESS_TOKEN_NAME, accessToken);

        if (code != null) {
            uriComponentsBuilder.queryParam(AREA_CODE_NAME, code);
        }

        URI areaUri = uriComponentsBuilder.encode()
                .build()
                .toUri();

        OpenApiTotalAreaResponse totalAreaResponse = restTemplate.getForObject(areaUri, OpenApiTotalAreaResponse.class);
        return totalAreaResponse.result();
    }

    private void fetchSigunguAndUmd(
            String accessToken,
            List<Area> allAreas,
            OpenApiAreaResponse sidoResponse
    ) {
        List<OpenApiAreaResponse> sigunguResponses = requestAreas(accessToken, sidoResponse.code());
        for (OpenApiAreaResponse sigunguResponse : sigunguResponses) {
            String sigungu = sigunguResponse.address();
            List<OpenApiAreaResponse> umdResponses = requestAreas(accessToken, sigunguResponse.code());
            addAreas(allAreas, sidoResponse.address(), sigungu, umdResponses);
        }
    }

    private void addAreas(
            List<Area> allAreas,
            String sido,
            String sigungu,
            List<OpenApiAreaResponse> umdResponses
    ) {
        for (OpenApiAreaResponse umdResponse : umdResponses) {
            String umd = umdResponse.address();
            allAreas.add(new Area(sido, sigungu, umd));
        }
    }
}
