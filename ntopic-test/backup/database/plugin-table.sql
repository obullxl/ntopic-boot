--
-- 全局序列数据表
--
CREATE TABLE nt_sequence (
  name      varchar(64) NOT NULL    COMMENT '序列名',
  version   bigint DEFAULT '1',
  step      bigint DEFAULT '10'     COMMENT '步长',
  value     bigint DEFAULT '1'      COMMENT '当前值',
  min_value bigint DEFAULT '1'      COMMENT '起始值',
  max_value bigint DEFAULT '9999999999' COMMENT '最大值',
  modify_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT 'NTopicX-全局序列数据表'
;


--
-- 全局配置数据表
--
CREATE TABLE nt_config (
  product       VARCHAR(64) NOT NULL    COMMENT '产品',
  module        VARCHAR(64) NOT NULL    COMMENT '模块',
  function      VARCHAR(64) NOT NULL    COMMENT '功能',
  config        LONGTEXT    DEFAULT '{}' COMMENT '配置',
  create_time   TIMESTAMP   NOT NULL DEFAULT '1988-08-08 08:08:08' COMMENT '创建时间',
  modify_time   TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (product, `module`, `function`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT 'NTopicX-全局配置数据表'
;


--
-- 分布式锁数据表
--
CREATE TABLE nt_lock (
  lock_id      varchar(64) NOT NULL     COMMENT '锁ID',
  `group`      varchar(64) DEFAULT 'DEFAULT' COMMENT '锁分组',
  version      bigint DEFAULT '0'       COMMENT '版本',
  lock_host    varchar(64)              COMMENT '锁定服务器',
  expire_time  varchar(64)              COMMENT '超时时间 yyyy-MM-dd HH:mm:ss.SSS格式',
  PRIMARY KEY (lock_id),
  INDEX nt_lock_group (`group`),
  INDEX nt_lock_expire_time (expire_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT 'NTopicX-分布式锁数据表'
;


--
-- 用户基本信息数据表
--
CREATE TABLE nt_user_base (
  id                VARCHAR(64)  NOT NULL      COMMENT '用户ID',
  `name`            VARCHAR(128) NOT NULL      COMMENT '用户名称',
  password          VARCHAR(64)  NOT NULL      COMMENT '密码',
  role_list         VARCHAR(256)               COMMENT '角色列表',
  ext_map           VARCHAR(4096) DEFAULT '{}' COMMENT 'KV扩展信息',
  create_time       TIMESTAMP  NOT NULL DEFAULT '1988-08-08 08:08:08' COMMENT '创建时间',
  modify_time       TIMESTAMP  NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (id),
  UNIQUE uk_name(`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT 'NTopicX-用户基本信息数据表'
;
