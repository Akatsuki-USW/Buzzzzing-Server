package bokjak.bokjakserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean
    public Executor asyncThreadTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(50); // 기본 스레드 수
        executor.setMaxPoolSize(100); // 최대 스레드 수
        executor.setQueueCapacity(200); // Queue 사이즈
        executor.setThreadNamePrefix("AsyncThread");
        return executor;
    }
}
