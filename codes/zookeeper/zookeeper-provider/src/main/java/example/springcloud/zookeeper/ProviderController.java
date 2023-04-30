package example.springcloud.zookeeper;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProviderController {

    private final DiscoveryClient discoveryClient;

    public ProviderController(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    @GetMapping({ "/", "/info" })
    public String info() {
        String info = "";
        List<ServiceInstance> instances = discoveryClient.getInstances("zookeeper-provider");
        for (ServiceInstance instance : instances) {
            info = instance.getUri().toString();
        }
        return info;
    }

}
