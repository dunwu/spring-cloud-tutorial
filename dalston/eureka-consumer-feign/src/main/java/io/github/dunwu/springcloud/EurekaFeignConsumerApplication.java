package io.github.dunwu.springcloud;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

/**
 * 通过 @EnableFeignClients 注解激活 Feign 服务
 * <p>
 * 通过 @EnableDiscoveryClient 注解激活 Eureka 中的 DiscoveryClient 实现
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2020-06-11
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class EurekaFeignConsumerApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(EurekaFeignConsumerApplication.class).web(true).run(args);
    }

}
