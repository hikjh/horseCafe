package stable.horseCafe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class HorseCafeApplication {

	public static void main(String[] args) {
		SpringApplication.run(HorseCafeApplication.class, args);
	}

}
