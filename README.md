## `wehappy` 是一个利用 `Spring Cloud` 微服务框架搭建的即时通信系统

### 环境搭建

#### `linux & mac`
1. 安装 `jdk11`, 不兼容 `jdk8`
2. 安装 `docker` 和 `docker-compose`
3. 运行 `start_db.sh` 启动 `mysql`
4. 连接 `mysql`
5. 执行 `config/sql` 下的两个数据库脚本
6. 进入 `config` 目录
7. 执行 `start_env.sh` 启动项目运行环境

### 启动服务
1. `auth`: `AuthApplication`, 鉴权服务
2. `user`: `UserApplication`, 用户服务
3. `sms`: `SmsApplication`, 邮件服务
4. `group`: `GroupApplication`, 群组服务
4. `chat`: `ChatApplication`, 聊天服务

### 访问swagger文档
1. `url`: `localhost:9001/doc.html`

#### 架构设计

![image-20201021005206055](doc/image/image-20201021005206055.png)

#### 详细功能：

![image-20201021005206013](doc/image/image-20201021005206013.png)

#### 数据库设计：

![image-20201021231238096](doc/image/image-20201021231238096.png)

