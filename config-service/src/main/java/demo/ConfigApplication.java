package demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.sleuth.sampler.AlwaysSampler;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigServer
public class ConfigApplication {

 	@Bean
  	public AlwaysSampler defaultSampler() {
    	return new AlwaysSampler();
  	}	
	
    public static void main(String[] args) {
        SpringApplication.run(ConfigApplication.class, args);
    }
}
