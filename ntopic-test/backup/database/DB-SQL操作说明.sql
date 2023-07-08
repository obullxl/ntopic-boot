ALTER TABLE nt_config ADD gmt_modify TIMESTAMP NOT NULL COMMENT '修改时间' AFTER config;
ALTER TABLE nt_config ADD gmt_create TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间' AFTER config;



ALTER TABLE nt_config DROP gmt_create;

INSERT INTO nt_config(product, module, function, config)
VALUES ('TEST-PRODUCT', 'TEST-MODULE', 'TEST-FUNCTION', '{}')
;

UPDATE nt_config SET gmt_create=NOW() WHERE product IS NOT NULL;

SELECT * FROM nt_config;

ALTER TABLE nt_config DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;



SHOW CREATE TABLE nt_user_base;

ALTER TABLE nt_user_base DROP gmt_create;
ALTER TABLE nt_user_base DROP gmt_modify;

ALTER TABLE nt_user_base ADD gmt_modify TIMESTAMP NOT NULL COMMENT '修改时间' AFTER ext_value;
ALTER TABLE nt_user_base ADD gmt_create TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间' AFTER ext_value;


alter table nt_user_base change role_enum role_list VARCHAR(256) COMMENT '角色列表';


ALTER TABLE nt_user_base ADD password VARChAR(64) NOT NULL COMMENT '密码' AFTER name;