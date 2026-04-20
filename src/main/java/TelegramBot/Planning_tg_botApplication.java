package TelegramBot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class Planning_tg_botApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Planning_tg_botApplication.class, args);
    }
}