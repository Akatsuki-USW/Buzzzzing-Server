package bokjak.bokjakserver.domain.user.model;

import lombok.Getter;
import org.springframework.http.HttpMethod;

@Getter
public enum SocialType {
    KAKAO(
            "kakao",
            "https://kapi.kakao.com/v2/user/me",
            HttpMethod.GET
    );

    private String socialName;
    private String socialUrl;
    private HttpMethod httpMethod;

    SocialType(String socialName, String socialUrl, HttpMethod httpMethod) {
        this.socialName = socialName;
        this.socialUrl = socialUrl;
        this.httpMethod = httpMethod;
    }
}
