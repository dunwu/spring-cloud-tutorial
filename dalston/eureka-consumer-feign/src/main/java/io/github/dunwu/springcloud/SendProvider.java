package io.github.dunwu.springcloud;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 通过 @FeignClient 指定 Eureka 服务，用于创建 Ribbon 负载均衡器
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2020-06-11
 */
@FeignClient("eureka-provider")
public interface SendProvider {

    @GetMapping("/send")
    String consumer();

}
