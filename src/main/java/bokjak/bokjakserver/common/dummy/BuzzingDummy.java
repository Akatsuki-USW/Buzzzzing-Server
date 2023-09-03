package bokjak.bokjakserver.common.dummy;

import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Transactional
@Profile({"local"}) // 특정 프로파일에만 더미 파일 작동
@interface BuzzingDummy {
}
