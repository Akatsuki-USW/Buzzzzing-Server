package bokjak.bokjakserver.common.dummy;

import bokjak.bokjakserver.domain.user.dto.AuthDto.SignUpRequest;
import bokjak.bokjakserver.domain.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Slf4j
@Component("userDummy")
@RequiredArgsConstructor
@BuzzingDummy
public class UserDummy {
    private final UserRepository userRepository;

    @PostConstruct
    public void init() {
        if (userRepository.count() > 0) {
            log.info("[userDummy] 더미 유저가 이미 존재합니다.");
        } else {
            createUsers();
            log.info("[userDummy] 더미 유저 생성완료");
        }
    }

    private void createUsers() {
        ArrayList<String> imageUrls = new ArrayList<>();
        imageUrls.add("https://s3-buz.s3.ap-northeast-2.amazonaws.com/user/profile/astro_pepe.jpeg");
        imageUrls.add("https://s3-buz.s3.ap-northeast-2.amazonaws.com/user/profile/classic_pepe.jpg");
        imageUrls.add("https://s3-buz.s3.ap-northeast-2.amazonaws.com/user/profile/crying_pepe.jpeg");
        imageUrls.add("https://s3-buz.s3.ap-northeast-2.amazonaws.com/user/profile/Feels_good_pepe.jpg");
        imageUrls.add("https://s3-buz.s3.ap-northeast-2.amazonaws.com/user/profile/gatsby_pepe.jpg");
        imageUrls.add("https://s3-buz.s3.ap-northeast-2.amazonaws.com/user/profile/health_pepe.jpeg");
        imageUrls.add("https://s3-buz.s3.ap-northeast-2.amazonaws.com/user/profile/heyatch_pepe.png");
        imageUrls.add("https://s3-buz.s3.ap-northeast-2.amazonaws.com/user/profile/laughing_pepe.jpg");
        imageUrls.add("https://s3-buz.s3.ap-northeast-2.amazonaws.com/user/profile/thinking_pepe.jpg");
        imageUrls.add("https://s3-buz.s3.ap-northeast-2.amazonaws.com/user/profile/punching_pepe.jpg");

        ArrayList<String> nicknames = new ArrayList<>();
        nicknames.add("astro_pepe");
        nicknames.add("classic_pepe");
        nicknames.add("crying_pepe");
        nicknames.add("Feels_good_pepe");
        nicknames.add("gatsby_pepe");
        nicknames.add("health_pepe");
        nicknames.add("heyatch_pepe");
        nicknames.add("laughing_pepe");
        nicknames.add("thinking_pepe");
        nicknames.add("punching_pepe");

        for (int i = 0; i < 10; i++) {
            SignUpRequest dummyUserForm = SignUpRequest.builder()
                    .build();
            userRepository.save(dummyUserForm.toDummy(nicknames.get(i) + "@naver.com", nicknames.get(i), i + "@KAKAO", imageUrls.get(i)));
        }
    }

}
