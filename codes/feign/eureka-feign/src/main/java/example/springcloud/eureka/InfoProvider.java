package example.springcloud.eureka;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("eureka-provider")
public interface InfoProvider {

    @GetMapping("/info")
    String info();

}
