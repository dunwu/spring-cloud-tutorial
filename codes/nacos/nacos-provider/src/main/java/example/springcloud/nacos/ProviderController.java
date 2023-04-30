package example.springcloud.nacos;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class ProviderController {

    private final DiscoveryClient discoveryClient;
    private final NacosDiscoveryProperties nacosDiscoveryProperties;

    public ProviderController(DiscoveryClient discoveryClient, NacosDiscoveryProperties nacosDiscoveryProperties) {
        this.discoveryClient = discoveryClient;
        this.nacosDiscoveryProperties = nacosDiscoveryProperties;
    }

    @GetMapping({ "/", "/info" })
    public String info() {
        String info = "";
        List<ServiceInstance> instances = discoveryClient.getInstances("nacos-provider");
        for (ServiceInstance instance : instances) {
            info = instance.getUri().toString();
        }
        return info;
    }

    @GetMapping("/metadata")
    public String zone() {
        Map<String, String> metadata = nacosDiscoveryProperties.getMetadata();
        return "provider zone " + metadata.get("zone");
    }

}
