package org.gcnc.calculate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	public static void main(String[] args) throws InterruptedException {
		// Thread.sleep(30000);
		SpringApplication.run(Application.class, args);
	}

	//Enable Global CORS support for the application
/*	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurerAdapter() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
						.allowedOrigins("*")
						.allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD")
						.allowedHeaders("header1", "header2") //What is this for?
						.allowCredentials(true);
			}
		};
	}
	*/
}
