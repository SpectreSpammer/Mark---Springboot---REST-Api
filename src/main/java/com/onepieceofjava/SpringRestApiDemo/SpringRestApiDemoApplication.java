package com.onepieceofjava.SpringRestApiDemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {
		"com.onepieceofjava.SpringRestApiDemo.controller",
		"com.onepieceofjava.SpringRestApiDemo.service",
		"com.onepieceofjava.SpringRestApiDemo.repository",
		"com.onepieceofjava.SpringRestApiDemo.security.config",
		"com.onepieceofjava.SpringRestApiDemo.security.service",
		"com.onepieceofjava.SpringRestApiDemo.security.repository",
		"com.onepieceofjava.SpringRestApiDemo.security.init"
})
@EntityScan({
		"com.onepieceofjava.SpringRestApiDemo.model",
		"com.onepieceofjava.SpringRestApiDemo.security.model"
})

@EnableJpaRepositories({
		"com.onepieceofjava.SpringRestApiDemo.repository",
		"com.onepieceofjava.SpringRestApiDemo.security.repository"
})
public class SpringRestApiDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringRestApiDemoApplication.class, args);
	}

}
