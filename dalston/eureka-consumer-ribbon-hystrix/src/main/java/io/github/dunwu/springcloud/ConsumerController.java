package io.github.dunwu.springcloud;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author 翟永超
 * @create 2017/4/15.
 * @blog http://blog.didispace.com
 */
@RestController
public class ConsumerController {

    private final ConsumerService consumerService;

    public ConsumerController(ConsumerService consumerService) {
        this.consumerService = consumerService;
    }

    @GetMapping("/recv")
    public String recv() {
        return consumerService.consumer();
    }

    /**
     * 利用 Hystrix 进行服务降级
     */
    @Service
    static class ConsumerService {

        private final RestTemplate restTemplate;

        public ConsumerService(RestTemplate restTemplate) {
            this.restTemplate = restTemplate;
        }

        @HystrixCommand(fallbackMethod = "fallback")
        public String consumer() {
            return restTemplate.getForObject("http://eureka-provider/send", String.class);
        }

        public String fallback() {
            return "fallback";
        }

    }

}
