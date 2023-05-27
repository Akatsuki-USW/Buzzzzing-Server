package bokjak.bokjakserver.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SocialService {

    private final RestTemplate restTemplate;

    protected ResponseEntity<Map<String, Object>> validOauthAccessToken(String oauthAccessToken, String socialUrl, HttpMethod httpMethod) {
        HttpHeaders headers = setHeaders(oauthAccessToken);
        HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<>(headers);
        ParameterizedTypeReference<Map<String, Object>> RESPONSE_TYPE =
                new ParameterizedTypeReference<Map<String, Object>>() {};
        return restTemplate.exchange(socialUrl,httpMethod,request,RESPONSE_TYPE);
    }

    private HttpHeaders setHeaders(String oauthAccessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + oauthAccessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }
}
