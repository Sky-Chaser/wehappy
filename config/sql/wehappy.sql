DROP DATABASE IF EXISTS `user_db`;
DROP DATABASE IF EXISTS `message_db`;
DROP DATABASE IF EXISTS `group_db`;
DROP DATABASE IF EXISTS `media_db`;
DROP DATABASE IF EXISTS `account_db`;

CREATE DATABASE `user_db` CHARACTER SET utf8;
CREATE DATABASE `message_db` CHARACTER SET utf8;
CREATE DATABASE `group_db` CHARACTER SET utf8;
CREATE DATABASE `media_db` CHARACTER SET utf8;
CREATE DATABASE `account_db` CHARACTER SET utf8;

USE `user_db`;

CREATE TABLE `user`
(
    `id`           BIGINT PRIMARY KEY,
    `email`        VARCHAR(70)  NOT NULL COMMENT '邮箱地址',
    `username`     VARCHAR(70)  NOT NULL COMMENT '用户名',
    `password`     VARCHAR(100) NOT NULL COMMENT '密码',
    `sex`          TINYINT      NOT NULL DEFAULT 0 COMMENT '性别：0表示男性，1表示女性，2表示未知',
    `avatar`       VARCHAR(200) NOT NULL COMMENT '头像链接',
    `gmt_create`   DATETIME     NOT NULL COMMENT '创建时间',
    `gmt_modified` DATETIME     NOT NULL COMMENT '更新时间',
    `status`       TINYINT      NOT NULL DEFAULT 1 COMMENT '状态：1表示正常，2表示被冻结',
    `number_like`  INT          NOT NULL DEFAULT 0 COMMENT '获赞个数',
    UNIQUE `ux_email` (`email`),
    UNIQUE `ux_username` (`username`)
) COMMENT = '用户信息表';

CREATE TABLE `friend`
(
    `id`           BIGINT PRIMARY KEY,
    `from_id`      BIGINT   NOT NULL COMMENT '用户id',
    `to_id`        BIGINT   NOT NULL COMMENT '好友id',
    `gmt_create`   DATETIME NOT NULL COMMENT '创建时间',
    `gmt_modified` DATETIME NOT NULL COMMENT '更新时间',
    `status`       TINYINT  NOT NULL DEFAULT 0 COMMENT '状态：0表示已申请还未添加，1表示正常，2表示拉黑对方',
    `black`        TINYINT  NOT NULL DEFAULT 0 COMMENT '是否被对方拉黑：0表示未拉黑，1表示已拉黑',
    KEY `ix_from_id_status` (`from_id`, `status`),
    UNIQUE `ux_from_id_to_id` (`from_id`, `to_id`)
) COMMENT = '好友信息表';

USE `message_db`;

CREATE TABLE `message`
(
    `id`           BIGINT PRIMARY KEY,
    `time`         BIGINT   NOT NULL COMMENT '时间戳',
    `type`         TINYINT  NOT NULL DEFAULT 0 COMMENT '消息类型：0表示文本消息，1表示系统消息，2表示图片，3表示语音，4表示视频，5表示语音通话，6表示视频通话，7表示私聊红包，8表示群聊普通红包，9表示群聊运气红包',
    `content`      text     NOT NULL COMMENT '消息内容',
    `gmt_create`   DATETIME NOT NULL COMMENT '创建时间',
    `gmt_modified` DATETIME NOT NULL COMMENT '更新时间',
    KEY `ix_gmt_create` (`gmt_create`)
) COMMENT = '消息表';

CREATE TABLE `conversation`
(
    `id`           BIGINT PRIMARY KEY,
    `type`         BIGINT   NOT NULL COMMENT '会话类型：0表示私聊，1表示群聊',
    `from`         BIGINT   NOT NULL COMMENT '发送者id',
    `to`           BIGINT   NOT NULL COMMENT '接收者id',
    `message_id`   BIGINT   NOT NULL COMMENT '消息id',
    `gmt_create`   DATETIME NOT NULL COMMENT '创建时间',
    `gmt_modified` DATETIME NOT NULL COMMENT '更新时间',
    KEY `ix_from_gmt_create` (`from`, `gmt_create`)
) COMMENT = '最近会话表';

CREATE TABLE `unread`
(
    `id`           BIGINT PRIMARY KEY,
    `user_id`      BIGINT   NOT NULL COMMENT '用户Id',
    `count`        INT      NOT NULL DEFAULT 0 COMMENT '总未读消息数',
    `gmt_create`   DATETIME NOT NULL COMMENT '创建时间',
    `gmt_modified` DATETIME NOT NULL COMMENT '更新时间',
    UNIQUE `ix_user_id` (`user_id`)
) COMMENT = '用户未读数表';

CREATE TABLE `conversation_unread`
(
    `id`                   BIGINT PRIMARY KEY,
    `conversation_id`      BIGINT   NOT NULL COMMENT '会话Id',
    `last_read_message_id` BIGINT   NOT NULL COMMENT '最后一条已读消息Id',
    `count`                INT      NOT NULL DEFAULT 0 COMMENT '未读消息数',
    `gmt_create`           DATETIME NOT NULL COMMENT '创建时间',
    `gmt_modified`         DATETIME NOT NULL COMMENT '更新时间',
    KEY `ix_conversation_id` (`conversation_id`)
) COMMENT = '会话未读数表';

CREATE TABLE `message_index`
(
    `id`           BIGINT PRIMARY KEY,
    `type`         TINYINT  NOT NULL COMMENT '消息类型：0表示私聊消息，1表示群聊消息，2表示推送消息',
    `from`         BIGINT   NOT NULL COMMENT '发送者id',
    `to`           BIGINT   NOT NULL COMMENT '接收者id：type为1时表示群聊Id',
    `message_id`   BIGINT   NOT NULL COMMENT '消息id',
    `gmt_create`   DATETIME NOT NULL COMMENT '创建时间',
    `gmt_modified` DATETIME NOT NULL COMMENT '更新时间',
    KEY `ix_from_to_message_id` (`from`, `to`, `message_id`),
    KEY `ix_from_to_gmt_create` (`from`, `to`, `gmt_create`),
    KEY `ix_to_gmt_create` (`to`, `gmt_create`)
) COMMENT = '消息索引表';

CREATE TABLE `message_delete`
(
    `id`                 BIGINT PRIMARY KEY,
    `type`               BIGINT   NOT NULL COMMENT '消息类型：0表示私聊消息，1表示群聊消息，2表示推送消息',
    `from`               BIGINT   NOT NULL COMMENT '发送者id',
    `to`                 BIGINT   NOT NULL COMMENT '接收者id：type为1时表示群聊Id',
    `message_index_id`   BIGINT   NOT NULL COMMENT '消息索引id',
    `gmt_create`         DATETIME NOT NULL COMMENT '创建时间',
    `gmt_modified`       DATETIME NOT NULL COMMENT '更新时间',
    UNIQUE `ux_from_to` (`from`, `to`),
    KEY `ix_gmt_create` (`gmt_create`)
) COMMENT = '消息删除记录表';

USE `group_db`;

CREATE TABLE `group`
(
    `id`           BIGINT PRIMARY KEY,
    `name`         varchar(50)  NOT NULL COMMENT '群名称',
    `admin_count`  INT          NOT NULL DEFAULT 1 COMMENT '管理员人数',
    `member_count` INT          NOT NULL DEFAULT 1 COMMENT '群人数',
    `owner_id`     BIGINT       NOT NULL DEFAULT 1 COMMENT '群主Id',
    `avatar`       varchar(200) NOT NULL COMMENT '头像链接',
    `status`       TINYINT      NOT NULL DEFAULT 0 COMMENT '状态：0表示正常，1表示被冻结',
    `gmt_create`   DATETIME     NOT NULL COMMENT '创建时间',
    `gmt_modified` DATETIME     NOT NULL COMMENT '更新时间',
    KEY `ix_name` (`name`)
) COMMENT = '群聊信息表';

CREATE TABLE `group_user`
(
    `id`              BIGINT PRIMARY KEY,
    `group_id`        BIGINT   NOT NULL COMMENT '群聊id',
    `user_id`         BIGINT   NOT NULL COMMENT '用户id',
    `invited_user_id` BIGINT   NOT NULL COMMENT '邀请用户id',
    `status`          TINYINT  NOT NULL DEFAULT 0 COMMENT '状态：0表示正常，1表示管理员邀请还未同意进群，2表示用户申请加群还未通过,3表示被禁言',
    `type`            TINYINT  NOT NULL COMMENT '用户类型：0表示普通群员，1表示管理员，2表示群主',
    `gmt_create`      DATETIME NOT NULL COMMENT '创建时间',
    `gmt_modified`    DATETIME NOT NULL COMMENT '更新时间',
    KEY `ix_user_id_type` (`user_id`, `type`),
    KEY `ix_group_id` (`group_id`)
) COMMENT = '群聊用户表';

USE `media_db`;

CREATE TABLE `image`
(
    `id`            BIGINT PRIMARY KEY,
    `url`           VARCHAR(100) NOT NULL COMMENT '文件路径',
    `thumbnail_url` VARCHAR(100) NOT NULL COMMENT '缩略图路径',
    `size`          BIGINT       NOT NULL COMMENT '大小，单位Byte',
    `md5`           VARCHAR(100) NOT NULL COMMENT 'md5值',
    `gmt_create`    DATETIME     NOT NULL COMMENT '创建时间',
    `gmt_modified`  DATETIME     NOT NULL COMMENT '更新时间',
    UNIQUE `ux_md5` (`md5`),
    KEY `ix_gmt_create` (`gmt_create`)
) COMMENT = '图片信息表';

CREATE TABLE `video`
(
    `id`            BIGINT PRIMARY KEY,
    `url`           VARCHAR(100) NOT NULL COMMENT '文件路径',
    `time`          INT          NOT NULL COMMENT '时长',
    `thumbnail_url` VARCHAR(100) NOT NULL COMMENT '封面路径',
    `size`          BIGINT       NOT NULL COMMENT '大小，单位Byte',
    `md5`           VARCHAR(100) NOT NULL COMMENT 'md5值',
    `gmt_create`    DATETIME     NOT NULL COMMENT '创建时间',
    `gmt_modified`  DATETIME     NOT NULL COMMENT '更新时间',
    UNIQUE `ux_md5` (`md5`),
    KEY `ix_gmt_create` (`gmt_modified`)
) COMMENT = '视频信息表';

CREATE TABLE `voice`
(
    `id`           BIGINT PRIMARY KEY,
    `url`          VARCHAR(100) NOT NULL COMMENT '文件路径',
    `time`         INT          NOT NULL COMMENT '时长',
    `size`         BIGINT       NOT NULL COMMENT '大小，单位Byte',
    `md5`          VARCHAR(100) NOT NULL COMMENT 'md5值',
    `gmt_create`   DATETIME     NOT NULL COMMENT '创建时间',
    `gmt_modified` DATETIME     NOT NULL COMMENT '更新时间',
    UNIQUE `ux_md5` (`md5`),
    KEY `ix_gmt_create` (`gmt_create`)
) COMMENT = '语音信息表';

CREATE TABLE `call`
(
    `id`           BIGINT PRIMARY KEY,
    `time`         INT      NOT NULL COMMENT '时长',
    `gmt_create`   DATETIME NOT NULL COMMENT '创建时间',
    `gmt_modified` DATETIME NOT NULL COMMENT '更新时间',
    KEY `ix_gmt_create` (`gmt_create`)
) COMMENT = '通话信息表';

USE `account_db`;

CREATE TABLE `account`
(
    `id`           BIGINT PRIMARY KEY,
    `user_id`      BIGINT   NOT NULL COMMENT '用户id',
    `money`        DECIMAL  NOT NULL DEFAULT 0 COMMENT '账户余额',
    `gmt_create`   DATETIME NOT NULL COMMENT '创建时间',
    `gmt_modified` DATETIME NOT NULL COMMENT '更新时间',
    `status`       TINYINT  NOT NULL DEFAULT 0 COMMENT '状态：0表示正常，1表示被冻结',
    UNIQUE `ux_user_id` (`user_id`)
) COMMENT = '账户表';

CREATE TABLE `big_red_envelope`
(
    `id`           BIGINT PRIMARY KEY,
    `user_id`      BIGINT            NOT NULL COMMENT '发送红包用户id',
    `money`        DECIMAL           NOT NULL COMMENT '总金额',
    `total`        INT               NOT NULL COMMENT '总份数',
    `remains`      INT               NOT NULL COMMENT '剩余份数',
    `type`         TINYINT DEFAULT 6 NOT NULL COMMENT '类型：7表示私聊红包，8表示普通红包，9表示运气红包',
    `gmt_create`   DATETIME          NOT NULL COMMENT '创建时间',
    `gmt_modified` DATETIME          NOT NULL COMMENT '更新时间',
    KEY `ix_gmt_create` (`gmt_create`)
) COMMENT = '大红包信息表';

CREATE TABLE `small_red_envelope`
(
    `id`                  BIGINT PRIMARY KEY,
    `big_red_envelope_id` BIGINT           NOT NULL COMMENT '大红包id',
    `money`               DECIMAL          NOT NULL COMMENT '金额',
    `user_id`             BIGINT DEFAULT 0 NOT NULL COMMENT '领取用户id',
    `gmt_create`          DATETIME         NOT NULL COMMENT '创建时间',
    `gmt_modified`        DATETIME         NOT NULL COMMENT '更新时间',
    UNIQUE `ux_big_red_envelope_id_user_id` (`big_red_envelope_id`, `user_id`)
) COMMENT = '小红包信息表';

INSERT INTO `user_db`.`user` (`id`, `email`, `username`, `password`, `sex`, `avatar`, `gmt_create`, `gmt_modified`,
                              `status`, `number_like`)
VALUES (1327499604039708673, '1093275610@163.com', 'lollipop',
        '$2a$10$vFmR4Xiv7tK4HkuYrkjwCeE1TjH7I06EBGlFCYlOwhFmVrDNwKXWq', 0, 'default.jpg', '2020-11-14 14:32:24',
        '2020-11-14 14:32:24', 1, 0);
INSERT INTO `user_db`.`user` (`id`, `email`, `username`, `password`, `sex`, `avatar`, `gmt_create`, `gmt_modified`,
                              `status`, `number_like`)
VALUES (1328198577420595202, 'nhuy@qq.com', 'hanggegreat',
        '$2a$10$CKkfkjouC/htFVivZFUF1u7cG75Vdx8zKovYIGbQ2maSV3i0QlrJa', 0, 'default.jpg', '2020-11-16 12:49:52',
        '2020-11-16 12:49:52', 1, 0);
