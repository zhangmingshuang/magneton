-- ----------------------------
-- 13、参数配置表
-- ----------------------------
drop table if exists admin_sys_config;
create table admin_sys_config
(
    id           int(5)       not null auto_increment comment '主键',
    config_name  varchar(100) not null default '' comment '参数名称',
    config_key   varchar(100) not null default '' comment '参数键名',
    config_value varchar(500) not null default '' comment '参数键值',
    built_in     bool         not null default false comment '系统内置（Y是 N否）',
    create_by    varchar(64)  not null default '' comment '创建者',
    create_time  datetime     not null comment '创建时间',
    update_by    varchar(64)  not null default '' comment '更新者',
    update_time  datetime     not null comment '更新时间',
    remark       varchar(500) not null default '' comment '备注',
    unique index idx_config_key (config_key),
    primary key (id)
) engine = innodb
  auto_increment = 100 comment = '参数配置表';