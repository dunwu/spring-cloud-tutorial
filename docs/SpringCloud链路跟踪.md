# SpringCloud 链路跟踪

Spring Cloud Sleuth 为 Spring Cloud 提供了分布式链路跟踪的解决方案。它大量借用了 Google Dapper、Zipkin 的设计。

## 简介

### 基本术语

Spring Cloud Sleuth 采用的是 Google 的开源项目 Dapper 的专业术语。

- **`Span（跨度）`** - 基本工作单元。发送一个远程调度任务就会产生一个 Span，Span 是一个 64 位 ID 唯一标识的，Trace 是用另一个 64 位 ID 唯一标识的，Span 还有其他数据信息，比如摘要、时间戳事件、Span 的 ID、以及进度 ID。
- **`Trace（跟踪）`** - 一系列 Span 组成的一个树状结构。请求一个微服务系统的 API 接口，这个 API 接口，需要调用多个微服务，调用每个微服务都会产生一个新的 Span，所有由这个请求产生的 Span 组成了这个 Trace。
- **`Annotation（标注）`** - 用来及时记录一个事件的，一些核心注解用来定义一个请求的开始和结束 。这些注解包括以下：
  - **`CS（Client Sent，即客户端发送）`** - 客户端发送一个请求，这个注解描述了这个 Span 的开始
  - **`SR（Server Received，即服务端接收）`** - 服务端获得请求并准备开始处理它，如果将其 sr 减去 cs 时间戳便可得到网络传输的时间。
  - **`SS（Server Sent，即服务端发送）`**–该注解表明请求处理的完成(当请求返回客户端)，如果 ss 的时间戳减去 sr 时间戳，就可以得到服务器请求的时间。
  - CR - Client Received （客户端接收响应）-此时 Span 的结束，如果 cr 的时间戳减去 cs 时间戳便可以得到整个请求所消耗的时间。

![](http://dunwu.test.upcdn.net/snap/20200615201920.png)

## 参考资料
