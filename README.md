# myrpc

#### 介绍
这是一个基于 Netty + Zookeeper + Kryo 的简易 RPC 框架<br>造轮子主要是为了巩固所学的知识，纸上得来终觉浅

#### 目录
以下是重要的包的简介：
```
|- myrpc
  |- annotation：包含了一些自定义的注解，用于服务端和客户端启动时自动扫描并注册
  |- extension：实现了一套自己的 SPI，参考 dubbo，做了简化
  |- codec: Netty解码器
  |- entity：测试用的bean
  |- factory: 单例工厂
  |- loadbalance: 负载均衡，多个服务应该如何选择。目前实现了随机和轮询
  |- registry: 注册中心，如 zk 注册中心
  |- serializer: 序列化，支持SPI拓展
  |- service: Provider提供的服务，测试用
  |- proxy: 代理，用于客户端代理，客户端调用服务接口，实际上是一个网络请求的过程
  |- transport: 网络相关，Netty 收发请求逻辑
  |- util: 工具类
 
```

#### 功能列表
- [x] 自定义 SPI 扩展
- [x] 动态代理
    - [x] JDK Proxy
    - [ ] Javassist 生成代码，直接调用
- [x] 注册中心
    - [x] Zookeeper
    - [ ] Eureka
    - [ ] Nacos
    - [ ] Consul
    - [ ] ...
- [x] 序列化
    - [x] Kryo
    - [x] fastjson
    - [ ] ...
- [ ] 压缩
    - [ ] gzip
    - [ ] ...
- [x] 远程通信
    - [x] 自定义通信协议
    - [x] 使用 Netty 框架
- [ ] 配置
    - [ ] JVM 参数配置
    - [ ] properties 文件配置
    - [ ] Apollo 动态配置
- [x] 负载均衡
    - [x] 随机策略
    - [x] 轮询策略
    - [ ] 一致性哈希
- [ ] 多版本
- [ ] 集群容错
    - [ ] 重试策略
    - [ ] 快速失败策略
- [x] 优雅停机（简易）
- [ ] 监控后台
- [ ] 线程模型

#### 运行
1. 环境要求：JDK8 以上、Lombok 插件
2. 需要安装 `Zookeeper` 并运行
3. 执行 TestServer 和 TestClient 即可

#### 参与贡献
1.  Fork 本仓库
2.  新建分支
3.  提交代码
4.  新建 Pull Request
