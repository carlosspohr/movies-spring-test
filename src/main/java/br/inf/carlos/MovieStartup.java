package br.inf.carlos;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class MovieStartup {

	public static void main(String[] args) {
		SpringApplication.run(MovieStartup.class, args);
	}
	
	@PostConstruct
    public void init(){
		System.out.println("MovieStartup.init() !!!!!");
    }
}
