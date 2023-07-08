CREATE TABLE nt_sequence
(
    name  VARCHAR(64) NOT NULL COMMENT '序列名称',
    value bigint      NOT NULL COMMENT '序列值',
    PRIMARY KEY (name)
) COMMENT='序列数据表'
;

CREATE TABLE nt_user_base
(
    id          varchar(64)  NOT NULL COMMENT '用户ID',
    name        varchar(128) NOT NULL COMMENT '用户名称',
    password    varchar(64)  NOT NULL COMMENT '密码',
    role_list   varchar(256)          DEFAULT NULL COMMENT '角色列表',
    ext_map     varchar(4096)         DEFAULT '{}' COMMENT 'KV扩展信息',
    create_time timestamp    NOT NULL DEFAULT '1988-08-08 08:08:08' COMMENT '创建时间',
    modify_time timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_name (name)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='NTopicX-用户基本信息数据表';