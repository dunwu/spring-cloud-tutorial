# SpringCloud 链路跟踪

## 简介

### 为什么需要链路跟踪

微服务架构，对外暴露的接口，实际上可能需要很多个服务协同才能完成这个接口功能。如果链路上任何一个服务出现问题或者网络超时，都会形成导致接口调用失败。随着业务的不断扩张，服务之间互相调用会越来越复杂。这让维护人员无法得知该请求是由某个或某些后端服务引起的，这时就需要解决如何**快读定位服务故障点**，以对症下药。

![img](http://dunwu.test.upcdn.net/snap/20200616145437.png)

因为以上原因，于是就有了分布式系统调用跟踪的诞生。 针对微服务化应用链路追踪的问题，Google 在 2010 年发表了论文[《Dapper, a Large-Scale Distributed Systems Tracing Infrastructure》](https://static.googleusercontent.com/media/research.google.com/zh-CN//archive/papers/dapper-2010-1.pdf)，这篇文章是业内实现链路追踪的标杆和理论基础，具有非常大的参考价值。

分布式跟踪系统还有其他比较成熟的实现，例如：Naver 的 Pinpoint、Apache 的 HTrace、阿里的鹰眼 Tracing、京东的 Hydra、新浪的 Watchman，美团点评的 CAT，skywalking 等。

## Sleuth

Spring Cloud Sleuth 提供链路追踪解决方案。

**链路追踪** - 通过 sleuth 可以很清楚的看出一个请求都经过了哪些服务；可以很方便的理清服务间的调用关系。

**可视化错误** - 对于程序未捕捉的异常，可以结合 zipkin 分析。

**分析耗时** - 通过 sleuth 可以很方便的看出每个采样请求的耗时，分析出哪些服务调用比较耗时。当服务调用的耗时随着请求量的增大而增大时，也可以对服务的扩容提供一定的提醒作用。

**优化链路** - 对于调用频繁的服务，可以并行调用或针对业务做一些优化措施等。

### 基本术语

Spring Cloud Sleuth 采用的是 Google 的开源项目 Dapper 的专业术语。

- **`Span（跨度）`** - 基本工作单元。发送一个远程调度任务就会产生一个 Span，Span 是一个 64 位 ID 唯一标识的，Trace 是用另一个 64 位 ID 唯一标识的，Span 还有其他数据信息，比如摘要、时间戳事件、Span 的 ID、以及进度 ID。
- **`Trace（跟踪）`** - 一系列 Span 组成的一个树状结构。请求一个微服务系统的 API 接口，这个 API 接口，需要调用多个微服务，调用每个微服务都会产生一个新的 Span，所有由这个请求产生的 Span 组成了这个 Trace。
- **`Annotation（标注）`** - 用来及时记录一个事件的，一些核心注解用来定义一个请求的开始和结束 。这些注解包括以下：
  - **`CS（Client Sent，即客户端发送请求）`** - 客户端发送一个请求，这个注解描述了这个 Span 的开始
  - **`SR（Server Received，即服务端接收响应）`** - 服务端获得请求并准备开始处理它，如果将其 sr 减去 cs 时间戳便可得到网络传输的时间。
  - **`SS（Server Sent，即服务端发送请求）`**– 该注解表明请求处理的完成(当请求返回客户端)，如果 ss 的时间戳减去 sr 时间戳，就可以得到服务器请求的时间。
  - **`CR （Client Received，即客户端接收响应）`** - 此时 Span 的结束，如果 cr 的时间戳减去 cs 时间戳便可以得到整个请求所消耗的时间。

![img](http://dunwu.test.upcdn.net/snap/20200615201920.png)

### 采样率

如果服务的流量很大，全部采集对传输、存储压力比较大。这个时候可以设置采样率，sleuth 可以通过配置 `spring.sleuth.sampler.probability=X.Y` (如配置为 1.0，则采样率为 100%，采集服务的全部追踪数据)，若不配置默认采样率是 0.1(即 10%)。也可以通过实现 bean 的方式来设置采样为全部采样(`AlwaysSampler`)或者不采样(`NeverSampler`)：如：

```java
@Bean public Sampler defaultSampler() { return new AlwaysSampler(); }
```

sleuth 采样算法的实现是 Reservoir sampling（水塘抽样）。实现类是 `PercentageBasedSampler`。

## Zipkin

**Zipkin 是一个基于 Java 开发的、开源的、分布式实时数据跟踪系统（Distributed Tracking System）**。它采集有助于解决服务架构中延迟问题的实时数据。

Zipkin 主要功能是聚集来自各个异构系统的实时监控数据。

![Zipkin 架构](http://dunwu.test.upcdn.net/snap/20200211155836.png)

### Zipkin Server

Zipkin Server 主要包括四个模块：

- **Collector** - 负责采集客户端传输的数据。
- **Storage** - 负责存储采集的数据。当前支持 Memory，MySQL，Cassandra，ElasticSearch 等，默认存储在内存中。
- **API（Query）** - 负责查询 Storage 中存储的数据。提供简单的 JSON API 获取数据，主要提供给 web UI 使用。
- **UI** - 提供简单的 web 界面。

### Zipkin Client

- **Tracer** - `Tracer` 存在于你的应用中，它负责采集关于已发生操作的实时元数据。它们通常会检测库，因此对于用户是透明的。例如，已检测的 Web 服务器记录它何时接收到请求，以及何时发送响应。收集的跟踪数据称为跨度（Span）。
- **Instrumentation** - Instrumentation 保证了生产环境的安全性和很少的开销。因此，它们仅在内部传播 ID，以告知接收方正在进行追踪。完成的 Span 将通过外部通信告知 Zipkin，类似于应用程序异步报告指标的方式。例如，当跟踪某个操作并且需要发出 http 请求时，会添加一些 header 来传播 ID。header 不用于发送详细信息，例如操作名称。
- **Reporter** - 能够将数据发送到 Zipkin 的检测应用程序中的组件，被称为 Reporter。Reporter 有多种传输方式，可以将跟踪数据发送到 Zipkin 采集器，后者将跟踪数据持久化保存到存储中。稍后，API 会查询存储以向 UI 提供渲染数据。

以下是 Zipkin 的一个示例工作流：

```shell
┌─────────────┐ ┌───────────────────────┐  ┌─────────────┐  ┌──────────────────┐
│ User Code   │ │ Trace Instrumentation │  │ Http Client │  │ Zipkin Collector │
└─────────────┘ └───────────────────────┘  └─────────────┘  └──────────────────┘
       │                 │                         │                 │
           ┌─────────┐
       │ ──┤GET /foo ├─▶ │ ────┐                   │                 │
           └─────────┘         │ record tags
       │                 │ ◀───┘                   │                 │
                           ────┐
       │                 │     │ add trace headers │                 │
                           ◀───┘
       │                 │ ────┐                   │                 │
                               │ record timestamp
       │                 │ ◀───┘                   │                 │
                             ┌─────────────────┐
       │                 │ ──┤GET /foo         ├─▶ │                 │
                             │X-B3-TraceId: aa │     ────┐
       │                 │   │X-B3-SpanId: 6b  │   │     │           │
                             └─────────────────┘         │ invoke
       │                 │                         │     │ request   │
                                                         │
       │                 │                         │     │           │
                                 ┌────────┐          ◀───┘
       │                 │ ◀─────┤200 OK  ├─────── │                 │
                           ────┐ └────────┘
       │                 │     │ record duration   │                 │
            ┌────────┐     ◀───┘
       │ ◀──┤200 OK  ├── │                         │                 │
            └────────┘       ┌────────────────────────────────┐
       │                 │ ──┤ asynchronously report span     ├────▶ │
                             │                                │
                             │{                               │
                             │  "traceId": "aa",              │
                             │  "id": "6b",                   │
                             │  "name": "get",                │
                             │  "timestamp": 1483945573944000,│
                             │  "duration": 386000,           │
                             │  "annotations": [              │
                             │--snip--                        │
                             └────────────────────────────────┘
```

Instrumented client 和 server 是分别使用了 ZipKin Client 的服务，Zipkin Client 会根据配置将追踪数据发送到 Zipkin Server 中进行数据存储、分析和展示。

## 案例

### 案例一

应用角色：

- **eureka-server** - 注册中心
- **sleuth-cr** - Sleuth 客户端接收方
- **sleuth-cs** - Sleuth 客户端发送方

### 案例二

应用角色：

- **eureka-server** - 注册中心
- **zipkin-server** - Zipkin 服务器
- **sleuth-cr** - Sleuth 客户端接收方
- **sleuth-cs** - Sleuth 客户端发送方

## 参考资料

- [《Dapper, a Large-Scale Distributed Systems Tracing Infrastructure》](https://static.googleusercontent.com/media/research.google.com/zh-CN//archive/papers/dapper-2010-1.pdf)
- [Spring Cloud Sleuth+Zipkin 原理分析](https://blog.csdn.net/zhllansezhilian/article/details/83001870)
