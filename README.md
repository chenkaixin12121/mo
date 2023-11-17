# mo

> 基于 java 17、Spring Boot 3、Spring Cloud 2022 的微服务开发脚手架，使用 Spring Authorization Server 提供对 OAuth2.1
> 的支持，自定义密码及验证码模式认证，集成 XXL-JOB、阿里云OSS、SMS、支付宝支付及微信支付。

#### 代码结构

```text
mo
├── mo-gateway  -- 网关服务
├── mo-auth     -- 认证中心
├── mo-pay      -- 支付中心
├── mo-system   -- 系统服务
├── mo-member   -- 会员服务
├── mo-common   -- 公共服务
├────── mo-common-apidoc
├────── mo-common-job
├────── mo-common-core 
├────── mo-common-feign
├────── mo-common-log
├────── mo-common-mybatis
├────── mo-common-redis
├────── mo-common-security
├────── mo-common-sms
├────── mo-common-oss
├────── mo-common-web
```

#### 技术栈

* Spring Boot、Spring Cloud、Spring Cloud Alibaba
* Spring Security、Spring Authorization Server
* MySQL、Redis、Mybatis Plus
* XXL-JOB

#### 启动步骤

1. 安装java17，mysql8，redis
2. 启动 nacos，导入 doc/nacos 中的配置，并修改 mo-common.yaml 中的用户名密码
3. 执行 doc/sql 脚本 创建对应数据库与表结构
4. 依次启动 mo-gateway、mo-auth ...
5. 查看文档：http://localhost:8010/doc.html