package bokjak.bokjakserver.common.dummy;

import bokjak.bokjakserver.domain.user.dto.AuthDto;
import bokjak.bokjakserver.domain.user.dto.AuthDto.SignUpRequest;
import bokjak.bokjakserver.domain.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component("userDummy")
@RequiredArgsConstructor
@Transactional
public class UserDummy {
    private final UserRepository userRepository;

    @PostConstruct
    public void init() {
        if (userRepository.count() > 0) {
            log.info("더미 유저가 이미 존재합니다.");
            return;
        }

        for (int i = 0; i < 10; i++) {
            SignUpRequest dummyUserForm = SignUpRequest.builder()
                    .build();
            userRepository.save(dummyUserForm.toDummy("dummy"+i+"@naver.com", "dummy"+i, i+"@KAKAO"));
        }
        log.info("더미 유저 생성완료");
    }

}
