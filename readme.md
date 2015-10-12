#RedisFlag
使用info命令实时生成监控图
+ 支持显示各种复杂集群结构表（分片、主从、子集群）
+ 支持监控TPS、内存使用量、命中率、阻塞Clients、连接数
+ 支持查看redis实例配置
+ 支持查看慢日志
+ 更多功能未完待续 ~ 

###使用说明
+ 环境要求：JRE1.7+ 、Servlet3.0+（因应用配置用到了ServletContainerInitializer，这种方式是Servlet3.0以上的版本才有的，所以应用容器必须支持Servlet3.0才能正常运行，比如tomcat7.0 +等等。。。）
+ redis节点配置文件：redis-cluster.json

####集群表
![Alt 集群表](doc/集群表.png)

####监控图
![Alt 监控图](doc/监控图.png)

####查看实例配置
![Alt 查看实例配置](doc/配置.png)

####查看慢日志
![Alt 查看慢日志](doc/SlowLog.png)