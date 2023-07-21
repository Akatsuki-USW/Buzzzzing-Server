package bokjak.bokjakserver.domain.location.repository;

import bokjak.bokjakserver.domain.congestion.model.CongestionLevel;
import bokjak.bokjakserver.domain.congestion.model.QCongestion;
import bokjak.bokjakserver.domain.location.model.Location;
import bokjak.bokjakserver.util.queries.OrderByNull;
import bokjak.bokjakserver.util.queries.SortOrder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.time.LocalDateTime;
import java.util.List;

import static bokjak.bokjakserver.domain.bookmark.model.QLocationBookmark.locationBookmark;
import static bokjak.bokjakserver.domain.category.model.QLocationCategory.locationCategory;
import static bokjak.bokjakserver.domain.congestion.model.QCongestion.congestion;
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
                .orderBy(specifyCongestionSortOrder(congestionLevelSortOrder), location.id.asc())
                .limit(pageable.getPageSize());

        return PageableExecutionUtils.getPage(
                query.fetch(),
                pageable,
                query::fetchCount
        );
    }

    @Override
    public Page<Location> getTopOfWeeklyAverageCongestion(Pageable pageable, LocalDateTime start, LocalDateTime end) {
        JPAQuery<Location> query = selectLocationsPrefix()
                .leftJoin(location.weeklyCongestionStatisticList, weeklyCongestionStatistic)
                .where(weeklyCongestionStatistic.createdAt.between(start, end))
                .orderBy(weeklyCongestionStatistic.averageCongestionLevel.asc())
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
                .limit(pageable.getPageSize());

        return PageableExecutionUtils.getPage(
                query.fetch(),
                pageable,
                query::fetchCount
        );
    }

    /* JPAQuery */
    public JPAQuery<Location> selectLocationsPrefix() {// 현재 혼잡도와 함께 로케이션 조회
        QCongestion subQCongestion = new QCongestion("subQCongestion");

        return queryFactory.selectFrom(location)
                .leftJoin(location.congestionList, congestion).fetchJoin()
                .leftJoin(location.locationCategory, locationCategory).fetchJoin()
                .leftJoin(location.locationBookmarkList, locationBookmark)
                .where(Expressions.list(congestion.location.id, congestion.observedAt) // 각 location의 가장 최근 congestion
                        .in(JPAExpressions
                                .select(subQCongestion.location.id, subQCongestion.observedAt.max())
                                .from(subQCongestion)
                                .groupBy(subQCongestion.location.id))
                );
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
            case ASC -> congestion.congestionLevel.gt(cursorCongestionLevel.getValue()) // 정렬 커서 페이징
                    .orAllOf(congestion.congestionLevel.eq(cursorCongestionLevel.getValue()), gtCursorId(cursorId));
            case DESC -> congestion.congestionLevel.lt(cursorCongestionLevel.getValue())
                    .orAllOf(congestion.congestionLevel.eq(cursorCongestionLevel.getValue()), gtCursorId(cursorId));
        };
    }

    private BooleanExpression containsKeyword(String keyword) {
        if (keyword == null) return null;
        else return location.name.contains(keyword);
    }

    private OrderSpecifier<?> specifyCongestionSortOrder(SortOrder congestionSortOrder) {
        if (congestionSortOrder == null) return OrderByNull.DEFAULT;    // null 혹은 "NORMAL" -> 정렬 생략
        else return switch (congestionSortOrder) {
            case ASC -> congestion.congestionLevel.asc();
            case DESC -> congestion.congestionLevel.desc();
        };
    }
}
