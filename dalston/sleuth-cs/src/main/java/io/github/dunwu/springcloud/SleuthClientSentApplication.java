package io.github.dunwu.springcloud;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@EnableDiscoveryClient
@SpringBootApplication
public class SleuthClientSentApplication {

    private final Logger logger = Logger.getLogger(getClass());

    @Bean
    @LoadBalanced
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @RequestMapping(value = "/sleuth-cs", method = RequestMethod.GET)
    public String trace() {
        logger.info("===<call sleuth-cs>===");
        return restTemplate().getForEntity("http://sleuth-cr/sleuth-cr", String.class).getBody();
    }

    public static void main(String[] args) {
        SpringApplication.run(SleuthClientSentApplication.class, args);
    }

}
