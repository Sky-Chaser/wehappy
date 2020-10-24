### wehappy 是一个即时通信系统

#### 环境搭建

1. 安装 `jdk`
2. 安装 `docker` 和 `docker-compose`
3. 进入 `config/docker` 目录
4. 执行命令：`docker-compose -f docker-compose-env.yaml mysql up -d`
5. 连接 `mysql`
6. 执行 `config/sql` 下的数据库脚本
7. 进入 `config/docker` 目录
8. 执行命令：`docker-compose -f docker-compose-env.yaml up -d`

#### 架构设计

TODO


#### 详细功能：

![image-20201021005206013](doc/image/image-20201021005206013.png)

#### 数据库设计：

![image-20201021231238096](doc/image/image-20201021231238096.png)

