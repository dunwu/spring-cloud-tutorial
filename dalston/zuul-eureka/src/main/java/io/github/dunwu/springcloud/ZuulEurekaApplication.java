package io.github.dunwu.springcloud;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@EnableZuulProxy
@SpringCloudApplication
public class ZuulEurekaApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ZuulEurekaApplication.class).web(true).run(args);
    }

}
