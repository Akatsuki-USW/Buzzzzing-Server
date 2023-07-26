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
import java.util.Optional;

import static bokjak.bokjakserver.domain.bookmark.model.QSpotBookmark.spotBookmark;
import static bokjak.bokjakserver.domain.category.model.QSpotCategory.spotCategory;
import static bokjak.bokjakserver.domain.location.model.QLocation.location;
import static bokjak.bokjakserver.domain.spot.model.QSpot.spot;
import static bokjak.bokjakserver.domain.spot.model.QSpotImage.spotImage;
import static bokjak.bokjakserver.domain.user.model.QUser.user;
import static bokjak.bokjakserver.domain.user.model.QUserBlockUser.userBlockUser;

@Slf4j
@RequiredArgsConstructor
public class SpotRepositoryImpl implements SpotRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    /**
     * 메인 메서드
     **/
    @Override
    public Page<Spot> findAllByLocationAndCategoriesExceptBlockedAuthors(
            Long userId,
            Pageable pageable,
            Long cursorId,
            Long locationId,
            List<Long> categoryIds
    ) {
        JPAQuery<Spot> query = selectSpotsExceptBlockedAuthorsPrefix(userId)
                .where(eqLocationId(locationId))// 특정 로케이션의
                .where(inSpotCategoryId(categoryIds))
                .where(ltCursorId(cursorId))    // 최신순
                .orderBy(spot.id.desc())
                .limit(pageable.getPageSize());

        return PageableExecutionUtils.getPage(
                query.fetch(),
                pageable,
                getSpotsByLocationAndCategoriesExceptBlockedTotalCountQuery(userId, locationId, categoryIds)::fetchOne
        );
    }

    @Override
    public Page<Spot> findAllByCategoriesExceptBlockedAuthors(Long userId, Pageable pageable, Long cursorId, List<Long> categoryIds) {
        JPAQuery<Spot> query = selectSpotsExceptBlockedAuthorsPrefix(userId)
                .where(inSpotCategoryId(categoryIds))   // 특정 카테고리의
                .where(ltCursorId(cursorId))    // 최신순
                .orderBy(spot.id.desc())
                .limit(pageable.getPageSize());

        return PageableExecutionUtils.getPage(
                query.fetch(),
                pageable,
                getSpotsByCategoriesExceptBlockedAuthorsCountQuery(userId, categoryIds)::fetchOne
        );
    }

    @Override
    public Page<Spot> findAllBookmarked(Pageable pageable, Long cursorId, Long userId) {
        JPAQuery<Spot> query = selectSpotsExceptBlockedAuthorsPrefix(userId)
                .where(spotBookmark.user.id.eq(userId))// 현재 유저가 북마크한
                .where(ltCursorId(cursorId))    // 최신순
                .orderBy(spot.id.desc())
                .limit(pageable.getPageSize());

        return PageableExecutionUtils.getPage(
                query.fetch(),
                pageable,
                selectBookmarkedSpotsTotalCountQuery(userId)::fetchOne
        );
    }

    @Override
    public Page<Spot> findAllMy(Pageable pageable, Long cursorId, Long userId) {
        JPAQuery<Spot> query = selectSpotsPrefix()
                .where(spot.user.id.eq(userId)) // 현재 유저가 작성한
                .where(ltCursorId(cursorId))    // 최신순
                .orderBy(spot.id.desc())
                .limit(pageable.getPageSize());

        return PageableExecutionUtils.getPage(
                query.fetch(),
                pageable,
                selectMySpotsTotalCountQuery(userId)::fetchOne
        );
    }

    @Override
    public Optional<Spot> findOne(Long spotId) {
        JPAQuery<Spot> query = selectSpotPrefix()
                .where(spot.id.eq(spotId));

        return Optional.ofNullable(query.fetchOne());
    }

    /**
     * JPA Query: Prefix
     **/
    private JPAQuery<Spot> selectSpotsExceptBlockedAuthorsPrefix(Long userId) {// 일반적인 리스트 조회
        return queryFactory.selectFrom(spot)
                .join(spot.user, user).fetchJoin()
                .join(spot.location, location).fetchJoin()
                .join(spot.spotCategory, spotCategory).fetchJoin()
                .leftJoin(spot.spotBookmarkList, spotBookmark).fetchJoin()
                .leftJoin(spot.spotImageList, spotImage)
                .where(spot.user.id.notIn(JPAExpressions.select(userBlockUser.blockedUser.id)  // 차단한 유저 제외
                        .from(userBlockUser)
                        .where(userBlockUser.blockerUser.id.eq(userId))
                ));
    }

    private JPAQuery<Spot> selectSpotsPrefix() { // 차단한 유저를 포함한 조회: 일반 리스트 조회 쿼리문보다 간단
        return queryFactory.selectFrom(spot)
                .join(spot.user, user).fetchJoin()
                .leftJoin(spot.spotBookmarkList, spotBookmark).fetchJoin()
                .leftJoin(spot.spotImageList, spotImage);
    }

    private JPAQuery<Spot> selectSpotPrefix() {
        return queryFactory.selectFrom(spot)
                .join(spot.user, user).fetchJoin()
                .join(spot.location, location).fetchJoin()
                .join(spot.spotCategory, spotCategory).fetchJoin()
                .leftJoin(spot.spotBookmarkList, spotBookmark).fetchJoin()
                .leftJoin(spot.spotImageList, spotImage);
    }

    /**
     * JPA Query: Count
     **/
    private JPAQuery<Long> getSpotsByLocationAndCategoriesExceptBlockedTotalCountQuery(Long userId, Long locationId, List<Long> categoryIds) {
        return selectSpotsExceptBlockedAuthorsCountPrefix(userId)
                .where(eqLocationId(locationId))
                .where(inSpotCategoryId(categoryIds));
    }

    private JPAQuery<Long> getSpotsByCategoriesExceptBlockedAuthorsCountQuery(Long userId, List<Long> categoryIds) {
        return selectSpotsExceptBlockedAuthorsCountPrefix(userId)
                .where(inSpotCategoryId(categoryIds));
    }

    private JPAQuery<Long> selectSpotsExceptBlockedAuthorsCountPrefix(Long userId) {// 카운트 쿼리에 쓰이는 Prefix
        return queryFactory.select(spot.count()).from(spot)
                .join(spot.user, user)
                .join(spot.location, location)
                .join(spot.spotCategory, spotCategory)
                .where(user.id.notIn(JPAExpressions.select(userBlockUser.blockedUser.id)  // 차단한 유저 제외
                        .from(userBlockUser)
                        .where(userBlockUser.blockerUser.id.eq(userId))
                ));
    }

    private JPAQuery<Long> selectBookmarkedSpotsTotalCountQuery(Long userId) {// getBookmarked의 totalElements
        return queryFactory.select(spot.count()).from(spot)
                .join(spot.user, user)
                .join(spot.location, location)
                .join(spot.spotCategory, spotCategory)
                .leftJoin(spot.spotBookmarkList, spotBookmark)
                .where(spot.user.id.notIn(JPAExpressions.select(userBlockUser.blockedUser.id)
                        .from(userBlockUser)
                        .where(userBlockUser.blockerUser.id.eq(userId))
                )).where(spotBookmark.user.id.eq(userId));
    }

    private JPAQuery<Long> selectMySpotsTotalCountQuery(Long userId) {// getMySpots의 totalElements
        return queryFactory.select(spot.count())
                .from(spot)
                .join(spot.user, user)
                .leftJoin(spot.spotBookmarkList, spotBookmark)
                .where(spot.user.id.eq(userId));
    }

    /**
     * BooleanExpression
     **/
    private BooleanExpression inSpotCategoryId(List<Long> categoryIdList) {
        return categoryIdList != null ? spotCategory.id.in(categoryIdList) : null;
    }

    private BooleanExpression ltCursorId(Long cursorId) {
        if (cursorId == null || cursorId == 0) return null;
        else return spot.id.lt(cursorId);
    }

    private BooleanExpression eqLocationId(Long locationId) {
        return locationId != null ? location.id.eq(locationId) : null;
    }
}