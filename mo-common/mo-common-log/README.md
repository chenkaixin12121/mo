#### 日志同步到 ELK

> 参考：https://blog.ckx.ink/2021/08/14/181033.html

1. 启动 ElasticSearch 和 LogStash
2. 修改 logback-spring.xml，添加 LOG_STASH 前缀的 appender
3. 配置 mo-common.yaml -> logstash.host，默认 localhost
4. 启动服务并测试