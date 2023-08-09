package dev.tripdraw.application.oauth;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;

import dev.tripdraw.domain.oauth.OauthType;
import dev.tripdraw.dto.auth.KakaoInfoResponse;
import dev.tripdraw.dto.auth.OauthInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class KakaoApiClient implements OauthClient {

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
        httpHeaders.set("Authorization", "Bearer " + accessToken);

        HttpEntity<?> request = new HttpEntity<>(httpHeaders);
        KakaoInfoResponse kakaoInfoResponse = restTemplate.postForObject(kakaoInfoUrl, request,
                KakaoInfoResponse.class);
        return kakaoInfoResponse.toOauthInfo();
    }
}
