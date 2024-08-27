package like.lion.way;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class WayApplication {

	public static void main(String[] args) {
		SpringApplication.run(WayApplication.class, args);
	}

}
