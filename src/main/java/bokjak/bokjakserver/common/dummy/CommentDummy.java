package bokjak.bokjakserver.common.dummy;

import bokjak.bokjakserver.domain.comment.model.Comment;
import bokjak.bokjakserver.domain.comment.repository.CommentRepository;
import bokjak.bokjakserver.domain.spot.model.Spot;
import bokjak.bokjakserver.domain.spot.repository.SpotRepository;
import bokjak.bokjakserver.domain.user.model.User;
import bokjak.bokjakserver.domain.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component("commentDummy")
@DependsOn("spotDummy")
@RequiredArgsConstructor
@BuzzingDummy
public class CommentDummy {
    private final UserRepository userRepository;
    private final SpotRepository spotRepository;
    private final CommentRepository commentRepository;

    @PostConstruct
    public void init() {
        if (commentRepository.count() > 0) {
            log.info("[commentDummy] 댓글 데이터가 이미 존재");
        } else {
            createComments();
            log.info("[commentDummy] 댓글 더미 생성 완료");
        }
    }

    private void createComments() {
        for (Spot spot : spotRepository.findAll()) {// 모든 스팟에 대해
            List<User> allUser = userRepository.findAll();

            for (int i = 0; i < 10; i++) {// 유저 10명
                Comment parent = commentRepository.save(Comment.builder()
                        .user(allUser.get(i))
                        .spot(spot)
                        .content("댓글" + spot.getId() * (i + 1))
                        .build());

                for (int j = 0; j < 5; j++) {
                    commentRepository.save(Comment.builder()
                            .user(allUser.get(j))
                            .spot(spot)
                            .parent(parent)
                            .content(parent.getId() + "의 대댓글")
                            .build());
                }
            }
        }
    }
}
