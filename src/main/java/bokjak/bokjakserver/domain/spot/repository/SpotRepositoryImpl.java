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
import java.util.Objects;
import java.util.Optional;

import static bokjak.bokjakserver.domain.bookmark.model.QSpotBookmark.spotBookmark;
import static bokjak.bokjakserver.domain.category.model.QSpotCategory.spotCategory;
import static bokjak.bokjakserver.domain.comment.model.QComment.comment;
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
        JPAQuery<Spot> query = selectFromSpotExceptBlockedAuthorsPrefix(userId)
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
        JPAQuery<Spot> query = selectFromSpotExceptBlockedAuthorsPrefix(userId)
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
        JPAQuery<Spot> query = selectFromSpotExceptBlockedAuthorsPrefix(userId)
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
        JPAQuery<Spot> query = selectFromSpotIncludingBlockedAuthorsPrefix()
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
    public Page<Spot> findAllCommentedByMeExceptBlockedAuthors(Pageable pageable, Long cursorId, Long userId) {
        // TODO: 응답값에 각 Spot의 lastCommentId 추가, 요청에서 받기 -> 쿼리 1회 감소
        Long latestCommentId = findLatestCommentIdBySpotId(cursorId);

        // TODO: JOIN을 못해서 default_batch_size 쿼리가 나가고 있다. 개선해보자
        JPAQuery<Spot> query = queryFactory
                .select(comment.spot).from(comment)
                .where(comment.user.id.eq(userId))  // 해당 유저의 댓글만 조회
                .groupBy(comment.spot.id)
                .having(ltMaxCommentId(latestCommentId))
                .orderBy(comment.id.max().desc())
                .limit(pageable.getPageSize());

        return PageableExecutionUtils.getPage(
                query.fetch(),
                pageable,
                findAllCommentedByMeExceptBlockedAuthorsCountQuery(userId)::fetchOne
        );
    }

    private Long findLatestCommentIdBySpotId(Long cursorId) {
        if (Objects.isNull(cursorId)) {
            return null;
        } else {
            Long latestCommentId = queryFactory.select(comment.id).from(comment)
                    .where(comment.spot.id.eq(cursorId))
                    .orderBy(comment.id.desc())
                    .fetchFirst();
            if (Objects.isNull(latestCommentId)) return null;

            return latestCommentId;
        }
    }

    @Override
    public Optional<Spot> findOne(Long spotId) {
        JPAQuery<Spot> query = selectFromSpotIncludingBlockedAuthorsWithBookmarkListFetchJoinedPrefix()
                .where(spot.id.eq(spotId));

        return Optional.ofNullable(query.fetchOne());
    }

    /**
     * JPA Query: Prefix
     **/
    private JPAQuery<Spot> selectFromSpotExceptBlockedAuthorsPrefix(Long userId) {// 일반적인 리스트 조회
        return queryFactory.selectFrom(spot)
                .join(spot.user, user).fetchJoin()
                .join(spot.location, location).fetchJoin()
                .join(spot.spotCategory, spotCategory).fetchJoin()
                .leftJoin(spot.spotBookmarkList, spotBookmark)
                .leftJoin(spot.spotImageList, spotImage)
                .where(spot.user.id.notIn(JPAExpressions.select(userBlockUser.blockedUser.id)  // 차단한 유저 제외
                        .from(userBlockUser)
                        .where(userBlockUser.blockerUser.id.eq(userId))
                ));
    }

    private JPAQuery<Spot> selectFromSpotIncludingBlockedAuthorsPrefix() { // 차단한 유저를 포함한 조회: 일반 리스트 조회 쿼리문보다 간단
        return queryFactory.selectFrom(spot)
                .join(spot.user, user).fetchJoin()
                .leftJoin(spot.spotBookmarkList, spotBookmark)
                .leftJoin(spot.spotImageList, spotImage);
    }

    private JPAQuery<Spot> selectFromSpotIncludingBlockedAuthorsWithBookmarkListFetchJoinedPrefix() {
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

    private JPAQuery<Long> findAllCommentedByMeExceptBlockedAuthorsCountQuery(Long userId) {
        return queryFactory.select(spot.count()).from(spot)
                .where(spot.user.id.notIn(JPAExpressions.select(userBlockUser.blockedUser.id)  // 차단한 유저 제외
                        .from(userBlockUser)
                        .where(userBlockUser.blockerUser.id.eq(userId))))
                .where(spot.id.in(JPAExpressions    // 현재 유저가 작성한 댓글들의 부모 스팟들
                        .select(comment.spot.id)
                        .from(comment)
                        .where(comment.user.id.eq(userId))));
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

    private BooleanExpression ltMaxCommentId(Long cursorId) {
        if (cursorId == null || cursorId == 0) return null;
        else return comment.id.max().lt(cursorId);
    }

    private BooleanExpression eqLocationId(Long locationId) {
        return locationId != null ? location.id.eq(locationId) : null;
    }
}
