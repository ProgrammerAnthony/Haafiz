Haafiz is a gateway in Micro Service which is inspired by many open source gateway :Zuul 1.x , spring cloud gateway，currently not released now ......

Haafiz （在英文中是守护者的意思）参考了很多开源网关的思想，基于Netty，Disruptor，etcd等技术做一个开源网关目前，目前还未发布正式版本
# Design
### Communication FrameWork
Netty

### Register Center 
Etcd

### Base Project FrameWork
Java

### Design HighLights
- asynchronized request &response
- Disruptor& MPMC（multi producer and multi consumer）
- Netty Reactor
- Make full use of cache (config&route rule&ReferenceConfig in dubbo&loadbalance strategy...)
- FilterChain design reference to Netty &Sentinal
- serialization for quick response data & parallelization for heavy time-consuming data

### core features
- authorization & authentication
- flow control
- black/white list 
- gray route support
- redirect 
- flow statistics

### architecture diagram
