package bokjak.bokjakserver.domain.notification.repository;

import bokjak.bokjakserver.domain.notification.model.Notification;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static bokjak.bokjakserver.domain.notification.model.QNotification.notification;
import static bokjak.bokjakserver.domain.user.model.QUser.user;

@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Notification> findLimitCountAndSortByRecent(Long userId) {
        JPAQuery<Notification> query = queryFactory.selectFrom(notification)
                .join(notification.user, user)
                .orderBy(notification.id.desc())
                .where(user.id.eq(userId))
                .limit(30);
        return query.fetch();
    }

    @Override
    public Optional<Notification> findByNotificationIdAndUserId(Long notificationId, Long userId) {
        JPAQuery<Notification> query = queryFactory.selectFrom(notification)
                .where(notification.id.eq(notificationId)
                        .and(user.id.eq(userId)));
        return Optional.ofNullable(query.fetchOne());
    }
}
