Haafiz is a gateway in Micro Service which is inspired by many open source gateway :Zuul 1.x , spring cloud gateway，currently not released now ......

Haafiz （在英文中是守护者的意思）参考了很多开源网关的思想，基于Netty，Disruptor，etcd等技术做一个开源网关目前，目前还未发布正式版本
# Design
### Communication FrameWork
Netty
Netty通信框架

### Register Center 
Etcd
Etcd注册中心

### Base Project FrameWork
Java
Java语言

### Design HighLights
- asynchronized request &response
- Disruptor& MPMC（multi producer and multi consumer）to support higher performance
- Netty Reactor
- load balance design refernce from Dubbo
- Make full use of cache (config&route rule&ReferenceConfig in dubbo&loadbalance strategy...)
- FilterChain design reference to Netty &Sentinal
- serialization for quick response data & parallelization for heavy time-consuming data
- OpenTracing protocol and RollingNumber to track request& response
### 设计亮点
- 异步请求响应
- Disruptor &MPMC高性能实现
- Netty Reactor线程模型
- 参考Dubbo的负载均衡设计
- 充分使用缓存（配置，路由规则，负载均衡策略）
- 参考 Sentinel和Netty的FilterChain 职责链模型设计
- 串行化和并行化优化
- 基于Opentracing和RollingNumber的请求，响应记录
### core features
- authorization & authentication
- flow control
- black/white list 
- gray route support
- redirect 
- flow statistics

### 核心功能
- 权限校验
- 流控
- 黑白名单
- 灰度路由
- 路由转发
- 流控统计

### architecture diagram
todo
### 架构图
todo
