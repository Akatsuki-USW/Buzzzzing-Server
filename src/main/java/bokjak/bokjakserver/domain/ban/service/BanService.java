package bokjak.bokjakserver.domain.ban.service;

import bokjak.bokjakserver.domain.ban.repository.BanRepository;
import bokjak.bokjakserver.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

import static bokjak.bokjakserver.domain.ban.dto.BanDto.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BanService {

    private final UserService userService;
    private final BanRepository banRepository;

    public BanListResponse getMyBanList(Long userId) {
        userService.getUser(userId);

        List<BanResponse> banResponseList = banRepository.findLimitCountAndSortByRecent(userId).stream()
                .map(BanResponse::of)
                .toList();

        return BanListResponse.of(banResponseList);
    }


}
