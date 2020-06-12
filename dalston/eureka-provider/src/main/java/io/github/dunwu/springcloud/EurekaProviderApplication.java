package io.github.dunwu.springcloud;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2020-05-27
 */
@EnableDiscoveryClient
@SpringBootApplication
public class EurekaProviderApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(EurekaProviderApplication.class).web(true).run(args);
    }

}
