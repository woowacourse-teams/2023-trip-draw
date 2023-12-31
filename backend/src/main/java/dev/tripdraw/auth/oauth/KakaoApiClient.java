package dev.tripdraw.auth.oauth;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;

import dev.tripdraw.auth.dto.KakaoInfoResponse;
import dev.tripdraw.auth.dto.OauthInfo;
import dev.tripdraw.common.auth.OauthType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class KakaoApiClient implements OauthClient {

    private static final String BEARER = "Bearer ";

    private final String kakaoInfoUrl;
    private final RestTemplate restTemplate;

    public KakaoApiClient(@Value("${oauth.kakao.info-url}") String kakaoInfoUrl, RestTemplate restTemplate) {
        this.kakaoInfoUrl = kakaoInfoUrl;
        this.restTemplate = restTemplate;
    }

    @Override
    public OauthType oauthType() {
        return OauthType.KAKAO;
    }

    @Override
    public OauthInfo requestOauthInfo(String accessToken) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(APPLICATION_FORM_URLENCODED);
        httpHeaders.set(AUTHORIZATION, BEARER + accessToken);

        HttpEntity<?> request = new HttpEntity<>(httpHeaders);
        KakaoInfoResponse kakaoInfoResponse = restTemplate.postForObject(kakaoInfoUrl, request,
                KakaoInfoResponse.class);
        return kakaoInfoResponse.toOauthInfo();
    }
}
