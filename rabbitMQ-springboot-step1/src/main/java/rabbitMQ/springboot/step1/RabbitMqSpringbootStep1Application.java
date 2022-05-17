package rabbitMQ.springboot.step1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RabbitMqSpringbootStep1Application {

	public static void main(String[] args) {
		SpringApplication.run(RabbitMqSpringbootStep1Application.class, args);
		System.out.println("hello world");
	}

}
