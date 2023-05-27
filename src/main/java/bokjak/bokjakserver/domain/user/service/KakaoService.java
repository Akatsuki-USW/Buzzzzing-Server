package bokjak.bokjakserver.domain.user.service;

import bokjak.bokjakserver.domain.user.dto.AuthDto.OAuthSocialEmailResponse;
import bokjak.bokjakserver.domain.user.model.SocialType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class KakaoService {

    private final RestTemplate restTemplate;
    private final SocialService socialService;

    protected OAuthSocialEmailResponse getKakaoId(String oauthAccessToken) {
        String socialUrl = SocialType.KAKAO.getSocialUrl();
        HttpMethod httpMethod = SocialType.KAKAO.getHttpMethod();
        ResponseEntity<Map<String, Object>> response =
                socialService.validOauthAccessToken(oauthAccessToken, socialUrl, httpMethod);
        Map<String, Object> oauthBody = response.getBody();
        String id = String.valueOf(oauthBody.get("id"));
        return OAuthSocialEmailResponse.to(id + "@KAKAO");
    }
}
