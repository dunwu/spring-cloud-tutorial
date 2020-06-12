package io.github.dunwu.springcloud;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2020-05-27
 */
@RestController
public class ConsumerController {

    private final LoadBalancerClient loadBalancerClient;
    private final RestTemplate restTemplate;

    public ConsumerController(LoadBalancerClient loadBalancerClient,
        RestTemplate restTemplate) {
        this.loadBalancerClient = loadBalancerClient;
        this.restTemplate = restTemplate;
    }

    @GetMapping("/recv")
    public String recv() {
        ServiceInstance serviceInstance = loadBalancerClient.choose("eureka-provider");
        String url = "http://" + serviceInstance.getHost() + ":" + serviceInstance.getPort() + "/send";
        System.out.println(url);
        return restTemplate.getForObject(url, String.class);
    }

}
