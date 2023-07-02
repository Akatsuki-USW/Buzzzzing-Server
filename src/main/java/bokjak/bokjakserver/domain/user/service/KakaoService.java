package bokjak.bokjakserver.domain.user.service;

import bokjak.bokjakserver.domain.user.dto.AuthDto.OAuthSocialEmailResponse;
import bokjak.bokjakserver.domain.user.dto.AuthDto.RevokeKakaoResponse;
import bokjak.bokjakserver.domain.user.model.SocialType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoService {

//    @Value("${kakao.AdminKey}") //달라고 말해야함 //todo
//    private String AdminKey;

    private final RestTemplate restTemplate;
    private final SocialService socialService;

    protected OAuthSocialEmailResponse getKakaoId(String oauthAccessToken) {
        String socialUrl = SocialType.KAKAO.getSocialUrl();
        HttpMethod httpMethod = SocialType.KAKAO.getHttpMethod();
        ResponseEntity<Map<String, Object>> response =
                socialService.validOauthAccessToken(oauthAccessToken, socialUrl, httpMethod);
        Map<String, Object> oauthBody = response.getBody();
        String id = String.valueOf(oauthBody.get("id"));
        log.info("id = {}", id);
        return OAuthSocialEmailResponse.to(id + "@KAKAO");
    }

    protected boolean revokeKakao(String socialUuid) {
        String unlinkUrl = "https://kapi.kakao.com/v1/user/unlink";
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        params.add("target_id_type", "user_id");
        params.add("target_id", socialUuid);
        log.info("socialUuid = {}", socialUuid);
        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "KakaoAK " + AdminKey);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);
        ResponseEntity<RevokeKakaoResponse> response = restTemplate.postForEntity(unlinkUrl, httpEntity, RevokeKakaoResponse.class);
        int revokeStatusCode = response.getStatusCode().value();
        return revokeStatusCode == 200;
    }
}