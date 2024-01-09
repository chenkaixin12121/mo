# mo

> 基于 java 17、Kotlin 2.0.0-Beta2、Spring Boot 3、Spring Cloud 2022 的微服务开发脚手架，使用 Spring Authorization Server 提供对 OAuth2.1
> 的支持，自定义密码及验证码模式认证，集成 XXL-JOB、阿里云OSS与SMS。

#### 代码结构

```text
mo
├── mo-gateway  -- 网关服务
├── mo-auth     -- 认证中心
├── mo-admin    -- 管理系统
├────── mo-admin-api
├────── mo-admin-service
├── mo-member   -- 会员服务
├────── mo-member-api
├────── mo-member-service
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

1. cd docker
2. docker compose -f .\docker-compose-env.yaml up -d，查看应用是否安装成功
3. docker compose -f .\docker-compose-app.yaml up -d，启动 mo 服务
4. http://localhost:8010/doc.html

#### 定时任务使用

1. 打开 http://localhost:5555/xxl-job-admin ，在执行器管理中修改示例执行器的 AppName 设置为 mo-admin
2. 选择自动注册，在执行器管理看到 "OnLine 机器地址" 已经注册上即可
3. 打开任务管理，执行一次任务，可以看到 mo-admin 控制台已经有一条成功的日志了