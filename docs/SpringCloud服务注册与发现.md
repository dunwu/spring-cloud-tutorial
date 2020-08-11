# SpringCloud服务注册与发现

## Eureka

![img](http://dunwu.test.upcdn.net/snap/20200717101131.png)

Eureka 是 Netflix 开源的 REST 服务发现组件，本身是一个 REST 服务。

Eureka 有两种角色：Client 和 Server，作用如下：

- Server 提供服务发现的能力，当 Client 启动时，会向 Server 注册自己的信息（如：IP、端口、服务名称等），Server 会存储这些信息。
- Client 启动后，会周期性向 Server 发送心跳以续约；超过一定时间，Server 如果仍未收到某注册实例的心跳，则注销改实例。
- 默认情况下，Server 也是 Client，因此多个 Server 实例，可以互相注册，来实现服务注册表中数据的副本。这种设计是 Server 的高可用方案。
- Client 会缓存服务注册表的信息。当 Server 宕机时，Client 依然可以从缓存中找到服务提供者，以缓解 Server 压力。

## Consul

Consul 是一个分布式高可用的系统，它包含多个组件，但是作为一个整体，在微服务架构中为我们的基础设施提供服务发现和服务配置的工具。它包含了下面几个特性：

- 服务发现
- 健康检查
- Key/Value 存储
- 多数据中心

#### 下载服务端

不同于 Eureka，Consual 的服务端直接下载并部署即可。下载地址：https://www.consul.io/

## 案例

### 案例：基本服务化调用

应用角色：

- **eureka-server** - 注册中心
  - 通过 `@EnableEurekaServer` 注解启动一个服务注册中心。
  - 启动后，访问：http://localhost:10001/，可以看到注册监控信息。
- **eureka-provider** - 服务提供方
  - 通过`@EnableDiscoveryClient` 注解激活 Eureka 中的 `DiscoveryClient` 实现。
  - 配置 `eureka.client.serviceUrl.defaultZone = http://localhost:10001/eureka/` 绑定注册中心。
- **eureka-consumer** - 服务消费方
  - 通过`@EnableDiscoveryClient` 注解激活 Eureka 中的 `DiscoveryClient` 实现。
  - 配置 `eureka.client.serviceUrl.defaultZone = http://localhost:10001/eureka/` 绑定注册中心。
  - 调用服务，代码如下：
    ```java
    ServiceInstance serviceInstance = loadBalancerClient.choose("eureka-provider");
    String url = "http://" + serviceInstance.getHost() + ":" + serviceInstance.getPort() + "/send";
    return restTemplate.getForObject(url, String.class);
    ```
  - 启动后，访问：http://localhost:10003/recv，可以看到从 eureka-provider 发来的信息。

## 参考资料

- [Consul 官网](https://www.consul.io/)
- http://blog.didispace.com/spring-cloud-starter-dalston-1/
- [深入理解Eureka](https://juejin.im/post/593cc4c25c497d006b876429)
