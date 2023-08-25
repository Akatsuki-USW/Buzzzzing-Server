package bokjak.bokjakserver.domain.comment.repository;

import bokjak.bokjakserver.domain.comment.model.Comment;
import bokjak.bokjakserver.domain.user.model.User;
import bokjak.bokjakserver.domain.user.model.UserStatus;
import bokjak.bokjakserver.util.CustomSliceExecutionUtils;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

import static bokjak.bokjakserver.domain.comment.model.QComment.comment;
import static bokjak.bokjakserver.domain.spot.model.QSpot.spot;
import static bokjak.bokjakserver.domain.user.model.QUser.user;
import static bokjak.bokjakserver.domain.user.model.QUserBlockUser.userBlockUser;

@Slf4j
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    /**
     * find
     **/
    @Override
    public Slice<Comment> findAllParentBySpotExceptBlockedAuthors(Pageable pageable, Long cursorId, Long spotId, Long userId) {
        JPAQuery<Comment> query = queryFactory.selectFrom(comment)
                .join(comment.spot, spot).fetchJoin()
                .join(comment.user, user).fetchJoin()
                .orderBy(comment.id.asc()) // 오래된 순
                .where(spot.id.eq(spotId)   // 특정 스팟의
                        .and(comment.parent.isNull())   // 부모 없는 댓글
                        .and(user.id.notIn(JPAExpressions   // 차단한 유저 제외
                                .select(userBlockUser.blockedUser.id)
                                .from(userBlockUser)
                                .where(userBlockUser.blockerUser.id.eq(userId))))
                        .and(gtCursorId(cursorId)))
                .limit(CustomSliceExecutionUtils.buildSliceLimit(pageable.getPageSize()));

        return CustomSliceExecutionUtils.getSlice(query.fetch(), pageable);
    }

    @Override
    public Slice<Comment> findAllChildrenByParentExceptBlockedAuthors(Pageable pageable, Long cursorId, Long parentId, Long userId) {
        JPAQuery<Comment> query = queryFactory.selectFrom(comment)
                .join(comment.user, user).fetchJoin()
                .orderBy(comment.id.asc()) // 오래된 순
                // DTO에서 parent id만 찾기 때문에 부모 댓글의 Q객체를 만들어 JOIN하지 않아도 따로 쿼리 발생하지 않음
                .where(comment.parent.id.eq(parentId)   // 특정 부모 댓글의
                        .and(user.id.notIn(JPAExpressions   // 차단한 유저 제외
                                .select(userBlockUser.blockedUser.id)
                                .from(userBlockUser)
                                .where(userBlockUser.blockerUser.id.eq(userId))))
                        .and(gtCursorId(cursorId)))
                .limit(CustomSliceExecutionUtils.buildSliceLimit(pageable.getPageSize()));

        return CustomSliceExecutionUtils.getSlice(query.fetch(), pageable);
    }

    @Override
    public List<User> findAllByparentCommentAndDistinctExceptParentAuthor(Long parentId, Long parentAuthorId) {
        JPAQuery<User> query = queryFactory.select(user)
                .from(user)
                .leftJoin(user.commentList, comment).fetchJoin()
                .where(comment.parent.id.eq(parentId)
                        .and(user.id.notIn(parentAuthorId))
                        .and(user.userStatus.eq(UserStatus.NORMAL)
                                .or(user.userStatus.eq(UserStatus.BANNED))));

        return query.fetch();
    }


    /**
     * count
     **/
    public Long countAllParentBySpotExceptBlockedAuthors(Long spotId, Long userId) {
        return queryFactory.select(comment.count()).from(comment)
                .where(comment.spot.id.eq(spotId)
                        .and(comment.parent.isNull())
                        .and(comment.user.id.notIn(JPAExpressions
                                .select(userBlockUser.blockedUser.id)
                                .from(userBlockUser)
                                .where(userBlockUser.blockerUser.id.eq(userId)))))
                .fetchOne();
    }

    public Long countAllChildrenByParentExceptBlockedAuthors(Long parentId, Long userId) {
        return queryFactory.select(comment.count()).from(comment)
                .where(comment.parent.id.eq(parentId)
                        .and(comment.user.id.notIn(JPAExpressions
                                .select(userBlockUser.blockedUser.id)
                                .from(userBlockUser)
                                .where(userBlockUser.blockerUser.id.eq(userId)))))
                .fetchOne();
    }


    /**
     * BooleanExpression
     **/
    private BooleanExpression gtCursorId(Long cursorId) {
        if (cursorId == null || cursorId == 0) return null;
        else return comment.id.gt(cursorId);
    }

}
