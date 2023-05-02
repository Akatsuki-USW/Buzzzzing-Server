package bokjak.bokjakserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BokjakserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(BokjakserverApplication.class, args);
	}

}
