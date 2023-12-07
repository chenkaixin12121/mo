#### 定时任务使用

1. 下载 https://github.com/xuxueli/xxl-job.git ，创建 xxl_job 数据库，执行 doc/db 下 sql 文件，修改配置文件
2. 在指定服务（如 mo-admin）下添加依赖 mo-common-job，修改 mo-common.yaml 的 xxl.job.address
3. 打开 http://your_ip:your_port/xxl-job-admin ，在执行器管理中修改示例执行器的 AppName 设置为 mo-admin
4. 启动 mo-admin 后在执行器管理看到 "OnLine 机器地址" 已经注册上即可
5. 打开任务管理，执行一次任务，可以看到 mo-admin 控制台已经有一条成功的日志了