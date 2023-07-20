package bokjak.bokjakserver.domain.spot.repository;

import bokjak.bokjakserver.domain.spot.model.Spot;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static bokjak.bokjakserver.domain.bookmark.model.QSpotBookmark.spotBookmark;
import static bokjak.bokjakserver.domain.category.model.QSpotCategory.spotCategory;
import static bokjak.bokjakserver.domain.spot.model.QSpot.spot;
import static bokjak.bokjakserver.domain.spot.model.QSpotImage.spotImage;
import static bokjak.bokjakserver.domain.user.model.QUser.user;
import static bokjak.bokjakserver.domain.user.model.QUserBlockUser.userBlockUser;

@Slf4j
@RequiredArgsConstructor
public class SpotRepositoryImpl implements SpotRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public Page<Spot> getSpots(
            Long userId,
            Pageable pageable,
            Long cursorId,
            Long locationId,
            List<Long> categoryIds
    ) {
        JPAQuery<Spot> query = selectSpotByLocationPrefix(userId, locationId)
                .where(gtCursorId(cursorId))
                .where(inSpotCategoryId(categoryIds))
                .limit(pageable.getPageSize());

        return PageableExecutionUtils.getPage(
                query.fetch(),
                pageable,
                query::fetchCount
        );
    }


    /* JPA Query */
    private JPAQuery<Spot> selectSpotByLocationPrefix(Long userId, Long locationId) {
        return queryFactory.selectFrom(spot)
                .leftJoin(spot.user, user).fetchJoin()
                .leftJoin(spot.spotCategory, spotCategory).fetchJoin()
                .leftJoin(spot.spotBookmarkList, spotBookmark).fetchJoin()
                .leftJoin(spot.spotImageList, spotImage)
                .where(spot.location.id.eq(locationId))
                .where(spot.user.id.notIn(JPAExpressions.select(userBlockUser.blockedUser.id)  // 차단한 유저 제외
                        .from(userBlockUser)
                        .where(userBlockUser.blockerUser.id.eq(userId))
                ));
    }

    private BooleanExpression inSpotCategoryId(List<Long> categoryIdList) {
        return categoryIdList != null ? spotCategory.id.in(categoryIdList) : null;
    }

    private BooleanExpression gtCursorId(Long cursorId) {
        if (cursorId == null || cursorId == 0) return null;
        else return spot.id.gt(cursorId);
    }

    private BooleanExpression notInUserId(List<Long> blockedUserIdList) {
        return blockedUserIdList != null ? spot.user.id.notIn(blockedUserIdList) : null;
    }
}
