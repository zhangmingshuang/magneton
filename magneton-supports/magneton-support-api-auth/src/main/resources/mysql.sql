create table api_auth_user
(
    id                int         not null auto_increment,
    account           varchar(32) not null comment '授权账号',
    pwd               char(32)    not null comment '密码',
    pwd_salt          varchar(32) not null comment '密码盐',
    status            tinyint     not null comment '状态',
    create_time       bigint      not null comment '创建时间',
    create_additional varchar(32) not null comment '创建附加信息',
    removed           tinyint     not null comment '是否已删除',
    additional        varchar(64) not null comment '附加信息',
    primary key (id)
) comment '用户';

create table api_auth_statistics
(
    id    int not null auto_increment,
    today int not null comment '天，yyyyMMdd',
    uv    int not null comment 'uv',
    pv    int not null comment 'pv',
    unique index idx_today (today),
    primary key (id)
) comment '授权统计';

create table api_auth_log
(
    id                int         not null auto_increment,
    user_id           int         not null comment '授权用户',
    create_time       bigint      not null comment '创建时间',
    create_additional varchar(32) not null comment '创建附加信息',
    index idx_create_time (create_time),
    primary key (id)
) comment '授权日志';