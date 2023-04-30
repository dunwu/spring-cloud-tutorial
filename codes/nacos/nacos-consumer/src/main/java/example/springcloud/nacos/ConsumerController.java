package example.springcloud.nacos;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ConsumerController {

    private final RestTemplate restTemplate;
    private final LoadBalancerClient loadBalancerClient;

    public ConsumerController(RestTemplate restTemplate, LoadBalancerClient loadBalancerClient) {
        this.restTemplate = restTemplate;
        this.loadBalancerClient = loadBalancerClient;
    }

    @GetMapping("/info")
    public String info() {
        ServiceInstance serviceInstance = loadBalancerClient.choose("nacos-provider");
        String url = serviceInstance.getUri().toString() + "/info";
        System.out.println("remote server url：" + url);
        return restTemplate.getForObject(url, String.class);
    }

}
