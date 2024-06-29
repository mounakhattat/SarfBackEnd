package trivaw.stage.sarf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class sarfApplication {

	public static void main(String[] args) {
		SpringApplication.run(sarfApplication.class, args);
	}

}
