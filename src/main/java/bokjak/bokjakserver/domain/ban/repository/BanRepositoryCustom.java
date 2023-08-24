package bokjak.bokjakserver.domain.ban.repository;

import bokjak.bokjakserver.domain.ban.model.Ban;

import java.util.List;

public interface BanRepositoryCustom {

    List<Ban> findLimitCountAndSortByRecent(Long userId);
}
