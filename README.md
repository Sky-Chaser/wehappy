## wehappy 是一个利用 `Spring Cloud` 微服务框架搭建的即时通信系统

### 环境搭建

#### `linux & mac`
1. 安装 `jdk11`
2. 安装 `docker` 和 `docker-compose`
3. 进入 `config/docker/env` 目录
4. 执行命令：`docker-compose mysql up -d`
5. 连接 `mysql`
6. 执行 `config/sql` 下的数据库脚本
7. 进入 `config/docker/env` 目录
8. 执行命令：`docker-compose up -d`

#### 架构设计

![image-20201021005206055](doc/image/image-20201021005206055.png)

#### 详细功能：

![image-20201021005206013](doc/image/image-20201021005206013.png)

#### 数据库设计：

![image-20201021231238096](doc/image/image-20201021231238096.png)

