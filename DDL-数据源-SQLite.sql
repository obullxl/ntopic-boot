CREATE TABLE nt_sequence
(
    name  VARCHAR(64) NOT NULL,
    value bigint      NOT NULL,
    PRIMARY KEY (name)
)
;

CREATE TABLE nt_user_base
(
    id          varchar(64)  NOT NULL,
    name        varchar(128) NOT NULL,
    password    varchar(64),
    role_list   varchar(256),
    ext_map     varchar(4096),
    create_time timestamp,
    modify_time timestamp,
    PRIMARY KEY (id),
    UNIQUE (name)
);