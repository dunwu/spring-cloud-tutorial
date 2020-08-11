# SpringCloud 服务网关

Zuul 是 Netflix 开源的一个 API Gateway 服务器, 本质上是一个 web servlet 应用。

Zuul 在云平台上提供动态路由，监控，弹性，安全等边缘服务的框架。Zuul 相当于是设备和 Netflix 流应用的 Web 网站后端所有请求的前门。

Zuul 的核心是一系列的 **filters**, 其作用可以类比 Servlet 框架的 Filter，或者 AOP。

Zuul 把 Request route 到 用户处理逻辑 的过程中，这些 filter 参与一些过滤处理，比如 Authentication，Load Shedding 等。

![img](http://dunwu.test.upcdn.net/snap/20200612074421.png)

Zuul 的功能特性：

- 身份认证
- 审查和监控
- 动态路由
- 压力测试
- 负载分配
- 静态响应处理
- 多区域弹性

## 参考资料
