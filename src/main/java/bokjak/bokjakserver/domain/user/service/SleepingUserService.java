package bokjak.bokjakserver.domain.user.service;

import bokjak.bokjakserver.domain.user.model.SleepingUser;
import bokjak.bokjakserver.domain.user.model.User;
import bokjak.bokjakserver.domain.user.repository.SleepingUserRepository;
import bokjak.bokjakserver.domain.user.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class SleepingUserService {

    private final UserRepository userRepository;
    private final SleepingUserRepository sleepingUserRepository;
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    // 1년 로그인 안할 시 휴면회원으로 이동시키는 스케쥴러
    @Scheduled(cron = "0 0 9 * * *")
    public void checkSleepingUser() {
        log.info("checkSleepingUser 스케쥴러 시작");
        LocalDateTime time = LocalDateTime.now().minusYears(1);

        //lastLoginDate가 1년이 지난 사람
        List<User> notLoginUser = userRepository.findByLastLoginDateBefore(time);
        moveToSleepUser(notLoginUser);
        log.info("checkSleepingUser 스케쥴러 끝");
    }

    private void moveToSleepUser(List<User> notLoginUser) {
        if (notLoginUser.isEmpty()) {
            log.info("추가된 휴면유저가 없습니다.");
        } else {
            for (User user : notLoginUser) {
                changeSleepingUser(user);
            }
        }
    }

    // 11개월 동안 접속하지 않은 유저들에게 휴면메일 보내는 스케쥴러
    @Scheduled(cron = "0 0 10 * * *")
    public void sendMailToBeforeSleep() {
        log.info("sendMailToBeforeSleep 스케쥴러 시작");
        LocalDateTime start = LocalDateTime.now().minusMonths(12);
        LocalDateTime end = LocalDateTime.now().minusMonths(11);
        List<User> beforeSleepUsers = userRepository.findByLastLoginDateBetween(start, end);

        sendMailToUsers(beforeSleepUsers);
        log.info("sendMailToBeforeSleep 스케쥴러 끝");
    }

    private void sendMailToUsers(List<User> beforeSleepUsers) {
        if (beforeSleepUsers.isEmpty()) log.info("휴면전환메일의 대상이 없습니다.");
        else {
            for (User beforeSleepUser : beforeSleepUsers) {
                sendMail(beforeSleepUser);
            }
        }
    }

    private void changeSleepingUser(User user) {
        SleepingUser sleepingUser = SleepingUser.builder()
                .originalId(user.getId())
                .role(user.getRole())
                .userStatus(user.getUserStatus())
                .email(user.getEmail())
                .password(user.getPassword())
                .profileImageUrl(user.getProfileImageUrl())
                .nickname(user.getNickname())
                .socialEmail(user.getSocialEmail())
                .socialType(user.getSocialType())
                .lastLoginDate(user.getLastLoginDate()).build();
        sleepingUserRepository.save(sleepingUser);

        user.changeSleepingUser();
    }


    private void sendMail(User beforeSleepingUser) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setSubject("복작복작 - 휴면계정으로 전환안내");
            helper.setTo(beforeSleepingUser.getEmail());
            ConcurrentHashMap<String, String> emailValues = new ConcurrentHashMap<>();
            emailValues.put("nickname", beforeSleepingUser.getNickname());

            Context context = new Context();
            emailValues.forEach((key, value) -> {
                context.setVariable(key, value);
            });

            String html = templateEngine.process("convertSleepingMail.html", context);
            helper.setText(html, true);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            log.warn("휴면계정 메일 전송에 실패했습니다.");
        }
    }
}
