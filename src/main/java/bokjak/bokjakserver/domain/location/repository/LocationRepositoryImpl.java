package bokjak.bokjakserver.domain.location.repository;

import bokjak.bokjakserver.domain.congestion.model.CongestionLevel;
import bokjak.bokjakserver.domain.location.model.Location;
import bokjak.bokjakserver.util.queries.OrderByNull;
import bokjak.bokjakserver.util.queries.SortOrder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static bokjak.bokjakserver.domain.bookmark.model.QLocationBookmark.locationBookmark;
import static bokjak.bokjakserver.domain.category.model.QLocationCategory.locationCategory;
import static bokjak.bokjakserver.domain.congestion.model.QWeeklyCongestionStatistic.weeklyCongestionStatistic;
import static bokjak.bokjakserver.domain.location.model.QLocation.location;

@Slf4j
@RequiredArgsConstructor
public class LocationRepositoryImpl implements LocationRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Location> search(
            Pageable pageable,
            Long cursorId,
            String keyword,
            List<Long> categoryIds,
            SortOrder congestionLevelSortOrder,
            CongestionLevel cursorCongestionLevel
    ) {
        JPAQuery<Location> query = selectLocationsPrefix()
                .where(containsKeyword(keyword))
                .where(congestionLevelSortOrder == null || cursorCongestionLevel == null // 정렬 여부에 따라 페이징 방식 다르게처리
                        ? gtCursorId(cursorId)
                        : gtCursor(cursorId, congestionLevelSortOrder, cursorCongestionLevel))
                .where(inLocationCategoryId(categoryIds))
                .orderBy(specifyCongestionSortOrder(congestionLevelSortOrder), location.id.asc())// 혼잡도, location_id 오름차순
                .limit(pageable.getPageSize());

        return PageableExecutionUtils.getPage(
                query.fetch(),
                pageable,
                query::fetchCount
        );
    }

    @Override
    public Page<Location> getLocations(Pageable pageable, Long cursorId) {
        JPAQuery<Location> query = queryFactory.selectFrom(location)
                .where(gtCursorId(cursorId))
                .orderBy(location.id.asc())// location_id 오름차순
                .limit(pageable.getPageSize());

        return PageableExecutionUtils.getPage(
                query.fetch(),
                pageable,
                query::fetchCount
        );
    }

    @Override
    public Optional<Location> getLocation(Long locationId) {
        JPAQuery<Location> query = queryFactory.selectFrom(location)
                .leftJoin(location.locationCategory, locationCategory).fetchJoin()
                .leftJoin(location.locationBookmarkList, locationBookmark).fetchJoin()
                .where(location.id.eq(locationId));

        return Optional.ofNullable(query.fetchOne());
    }

    @Override
    public Page<Location> getTopOfWeeklyAverageCongestion(Pageable pageable, LocalDateTime start, LocalDateTime end) {
        JPAQuery<Location> query = selectLocationsPrefix()
                .leftJoin(location.weeklyCongestionStatisticList, weeklyCongestionStatistic)
                .where(weeklyCongestionStatistic.createdAt.between(start, end))
                .orderBy(weeklyCongestionStatistic.averageCongestionLevel.asc(), location.id.asc()) // 평균 혼잡도 오름차순, location_id 오름차순
                .limit(pageable.getPageSize());

        return PageableExecutionUtils.getPage(
                query.fetch(),
                pageable,
                query::fetchCount
        );
    }

    @Override
    public Page<Location> getBookmarked(Pageable pageable, Long cursorId, Long userId) {
        JPAQuery<Location> query = selectLocationsPrefix()
                .where(locationBookmark.user.id.eq(userId))// 특정 user의 Bookmark와 JOIN
                .where(gtCursorId(cursorId))
                .orderBy(location.id.asc()) // location_id 오름차순
                .limit(pageable.getPageSize());

        return PageableExecutionUtils.getPage(
                query.fetch(),
                pageable,
                query::fetchCount
        );
    }

    /* JPAQuery */
    public JPAQuery<Location> selectLocationsPrefix() {// 로케이션 조회
        return queryFactory.selectFrom(location)
                .leftJoin(location.locationCategory, locationCategory).fetchJoin()
                .leftJoin(location.locationBookmarkList, locationBookmark).fetchJoin();
    }

    private BooleanExpression inLocationCategoryId(List<Long> categoryIdList) {
        return categoryIdList != null ? locationCategory.id.in(categoryIdList) : null;
    }

    private BooleanExpression gtCursorId(Long cursorId) {
        if (cursorId == null || cursorId == 0) return null;
        else return location.id.gt(cursorId);
    }

    private BooleanExpression gtCursor(Long cursorId, SortOrder congestionSortOrder, CongestionLevel cursorCongestionLevel) {
        return switch (congestionSortOrder) {// ASC -> gt  DESC -> lt
            case ASC -> location.realtimeCongestionLevel.gt(cursorCongestionLevel.getValue()) // 정렬 커서 페이징
                    .orAllOf(location.realtimeCongestionLevel.eq(cursorCongestionLevel.getValue()), gtCursorId(cursorId));
            case DESC -> location.realtimeCongestionLevel.lt(cursorCongestionLevel.getValue())
                    .orAllOf(location.realtimeCongestionLevel.eq(cursorCongestionLevel.getValue()), gtCursorId(cursorId));
        };
    }

    private BooleanExpression containsKeyword(String keyword) {
        if (keyword == null) return null;
        else return location.name.contains(keyword);
    }

    private OrderSpecifier<?> specifyCongestionSortOrder(SortOrder congestionSortOrder) {
        if (congestionSortOrder == null) return OrderByNull.DEFAULT;    // null 혹은 "NORMAL" -> 정렬 생략
        else return switch (congestionSortOrder) {
            case ASC -> location.realtimeCongestionLevel.asc();
            case DESC -> location.realtimeCongestionLevel.desc();
        };
    }
}
