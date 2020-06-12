# SpringCloud 声明式 REST 调用

## Feign

Feign 是 Netflix 开发的声明式、模板化的 HTTP 客户端。只需要通过创建接口并用注解来配置它就可完成对 Web 服务接口的绑定。

- 它具备可插拔的注解支持，包括 Feign 注解、JAX-RS 注解。它也支持可插拔的编码器和解码器。
- Spring Cloud Feign 还扩展了对 Spring MVC 注解的支持，同时还整合了 Ribbon 和 Eureka 来提供均衡负载的 HTTP 客户端实现。

## 案例

### 案例：Feign

应用角色：

- **eureka-server** - 注册中心
- **eureka-provider** - 服务提供方
- **eureka-consumer-feign** - 引入 Feign 的服务消费方
  - 通过 `@EnableDiscoveryClient` 注解激活 Eureka 中的 `DiscoveryClient` 实现。
  - 通过 `@EnableFeignClients` 注解激活 Feign 服务。
  - 通过 `@FeignClient` 指定 Eureka 服务，用于创建 Ribbon 负载均衡器。创建一个 Feign 的客户端接口定义
    ```java
  @FeignClient("eureka-provider")
    public interface SendProvider {
  
      @GetMapping("/send")
        String consumer();
  
    }
    ```
  - 调用服务，代码如下：
    ```java
  @Autowired
    private SendProvider sendProvider;
  
    public ConsumerController(SendProvider sendProvider) {
      this.sendProvider = sendProvider;
    }
  
    @GetMapping("/recv")
    public String recv() {
        return sendProvider.consumer();
    }
    ```
  - 配置 `eureka.client.serviceUrl.defaultZone = http://localhost:10001/eureka/` 绑定注册中心。

## 参考资料

- [Consul 官网](https://www.consul.io/)
- http://blog.didispace.com/spring-cloud-starter-dalston-1/
