package bokjak.bokjakserver.domain.comment.repository;

import bokjak.bokjakserver.domain.comment.model.Comment;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import static bokjak.bokjakserver.domain.comment.model.QComment.comment;
import static bokjak.bokjakserver.domain.spot.model.QSpot.spot;
import static bokjak.bokjakserver.domain.user.model.QUser.user;
import static bokjak.bokjakserver.domain.user.model.QUserBlockUser.userBlockUser;

@Slf4j
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Comment> findAllBySpotExceptBlockedAuthors(Pageable pageable, Long cursorId, Long spotId, Long userId) {
        JPAQuery<Comment> query = queryFactory.selectFrom(comment)
                .join(comment.spot, spot).fetchJoin()
                .join(comment.user, user).fetchJoin()
                .orderBy(comment.id.desc()) // 최신순
                .where(spot.id.eq(spotId)   // 특정 스팟의
                        .and(user.id.notIn(JPAExpressions   // 차단한 유저 제외
                                .select(userBlockUser.blockedUser.id)
                                .from(userBlockUser)
                                .where(userBlockUser.blockerUser.id.eq(userId))))
                        .and(ltCursorId(cursorId)))
                .limit(pageable.getPageSize());

        return PageableExecutionUtils.getPage(
                query.fetch(),
                pageable,
                query::fetchCount   // TODO refactor 커서 Pageable 구현시 삭제
        );
    }

    /**
     * BooleanExpression
     **/
    private BooleanExpression ltCursorId(Long cursorId) {
        if (cursorId == null || cursorId == 0) return null;
        else return comment.id.lt(cursorId);
    }

}
