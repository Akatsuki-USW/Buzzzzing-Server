package bokjak.bokjakserver.domain.ban.repository;

import bokjak.bokjakserver.domain.ban.model.Ban;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static bokjak.bokjakserver.domain.ban.model.QBan.ban;

@RequiredArgsConstructor
public class BanRepositoryImpl implements BanRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Ban> findLimitCountAndSortByRecent(Long userId) {
        JPAQuery<Ban> query = queryFactory.selectFrom(ban)
                .where(ban.user.id.eq(userId))
                .orderBy(ban.id.asc())
                .limit(10);

        return query.fetch();
    }
}
