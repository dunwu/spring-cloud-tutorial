# 使用 Eureka 作为注册中心

## 使用说明

（1）添加依赖到 `pom.xml`

```xml
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
</dependency>
```

（2）添加 `@EnableEurekaServer` 注解到启动类上。

```java
@EnableEurekaServer
@SpringBootApplication
public class EurekaServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class, args);
    }

}
```

（3）添加配置到 `application.properties`

默认，Eureka 会将自己作为客户端来尝试向自己注册，如果要禁用，可以采用如下配置：

```properties
eureka.client.register-with-eureka = false
eureka.client.fetch-registry = false
```

（4）执行启动类 `EurekaServerApplication`

（5）启动成功后，在浏览器中访问：http://localhost:10001/

![](https://raw.githubusercontent.com/dunwu/images/dev/snap/20230422163928.png)

## 参考资料

https://github.com/spring-cloud-samples/eureka