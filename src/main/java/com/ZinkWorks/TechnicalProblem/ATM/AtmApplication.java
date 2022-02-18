package com.ZinkWorks.TechnicalProblem.ATM;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.ZinkWorks.TechnicalProblem"})
@EntityScan("com.ZinkWorks.TechnicalProblem")
@ComponentScan(basePackages = "com.ZinkWorks.TechnicalProblem")
public class AtmApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(AtmApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(AtmApplication.class, args);
	}

}
