package parqueadero;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EnableAutoConfiguration
@EntityScan(basePackages = {"parqueadero.entidad"})
public class ParqueaderoPruebaApplication {

	
	public static void main(String[] args) {
		SpringApplication.run(ParqueaderoPruebaApplication.class, args);
	}
}
