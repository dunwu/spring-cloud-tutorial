# Spring Cloud 负载均衡

## Ribbon

> **Ribbon 是基于 Netflix 实现的一套 HTTP 和 TCP 的客户端负载均衡器**。
>
> 为 Ribbon 配置服务提供者地址列表后，Ribbon 可以基于某种负载均衡算法，自动地帮助服务消费者去请求。Ribbon 内置了轮询、随机等负载均衡算法，用户也可以自定义负载均衡算法。

在 Spring Cloud 中，Ribbon 与 Eureka 配合使用 ，Ribbon 就可以自动从 Eureka Server 获取服务提供者地址列表。

![img](http://dunwu.test.upcdn.net/snap/20200611204136.png)

- 当 Ribbon 与 Eureka 联合使用时，ribbonServerList 会被 `DiscoveryEnabledNIWSServerList` 重写，扩展成从 Eureka 注册中心中获取服务实例列表。同时它也会用 `NIWSDiscoveryPing` 来取代 `IPing`，它将职责委托给 Eureka 来确定服务端是否已经启动。
- 而当 Ribbon 与 Consul 联合使用时，ribbonServerList 会被 `ConsulServerList` 来扩展成从 Consul 获取服务实例列表。同时由 `ConsulPing` 来作为 `IPing` 接口的实现。

下面我们通过具体的例子来看看如何使用 Spring Cloud Ribbon 来实现服务的调用以及客户端均衡负载。

## 案例

### 案例：Eureka + Ribbon 负载均衡

应用角色：

- **eureka-server** - 注册中心
- **eureka-provider** - 服务提供方
- **eureka-consumer-ribbon** - 引入 Ribbon 的服务消费方
  - 通过`@EnableDiscoveryClient` 注解激活 Eureka 中的 `DiscoveryClient` 实现。
  - 配置 `eureka.client.serviceUrl.defaultZone = http://localhost:10001/eureka/` 绑定注册中心。
  - 为 `RestTemplate` 增加 `@LoadBalanced` 注解。当使用 `RestTemplate` 这个实例发起 HTTP 请求时，会自动进行负载均衡。
    ```java
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    ```
  - 调用服务，代码如下：
    ```java
    @GetMapping("/recv")
    public String recv() {
        return restTemplate.getForObject("http://eureka-provider/send", String.class);
    }
    ```
  - 启动后，访问：http://localhost:10004/recv，可以看到从 eureka-provider 发来的信息。

## 参考资料

- [Consul 官网](https://www.consul.io/)
- http://blog.didispace.com/spring-cloud-starter-dalston-1/
