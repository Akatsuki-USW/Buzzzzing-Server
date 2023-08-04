package bokjak.bokjakserver.domain.ban.service;

import bokjak.bokjakserver.common.exception.StatusCode;
import bokjak.bokjakserver.domain.ban.exception.BanException;
import bokjak.bokjakserver.domain.user.model.User;
import bokjak.bokjakserver.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static bokjak.bokjakserver.domain.ban.dto.BanDto.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BanService {

    private final UserRepository userRepository;

    public BanListResponse getMyBanList(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BanException(StatusCode.NOT_FOUND_USER));

        return  BanListResponse.of(user.getBanList().stream()
                .map(BanResponse::of)
                .sorted((n1, n2) -> n2.banStartAt().compareTo(n1.banStartAt()))
                .limit(10)
                .toList()
        );
    }


}
