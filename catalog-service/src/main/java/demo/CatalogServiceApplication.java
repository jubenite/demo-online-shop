package demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.sleuth.sampler.AlwaysSampler;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableEurekaClient
@EnableHystrix
public class CatalogServiceApplication {

//	@Bean
//	GraphiteReporter graphiteReporter(MetricRegistry registry,
//			@Value("${graphite.host}") String host,
//			@Value("${graphite.port}") int port) {
//		GraphiteReporter reporter = GraphiteReporter.forRegistry(registry)
//				.prefixedWith("catalog-service").build(new Graphite(host, port));
//		reporter.start(2, TimeUnit.SECONDS);
//		return reporter;
//	}

	@Bean
	public AlwaysSampler defaultSampler() {
		return new AlwaysSampler();
	}

	public static void main(String[] args) {
		SpringApplication.run(CatalogServiceApplication.class, args);
	}

	@LoadBalanced
	@Bean
	public RestTemplate loadRestTemplate() {
		return new RestTemplate();
	}
}