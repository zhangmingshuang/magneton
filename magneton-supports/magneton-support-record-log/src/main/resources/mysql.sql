-- ----------------------------
-- 10、操作日志记录
-- ----------------------------
drop table if exists sys_oper_log;
create table sys_oper_log
(
    id             bigint(20)    not null auto_increment comment '主键',
    title          varchar(50)   not null default '' comment '模块标题',
    business_type  int(2)        not null default 0 comment '业务类型（0其它 1新增 2修改 3删除）',
    method         varchar(100)  not null default '' comment '方法名称',
    request_method varchar(10)   not null default '' comment '请求方式',
    operator_type  int(1)        not null default 0 comment '操作类别（0其它 1后台用户 2手机端用户）',
    oper_name      varchar(50)   not null default '' comment '操作人员',
    dept_name      varchar(50)   not null default '' comment '部门名称',
    oper_url       varchar(255)  not null default '' comment '请求URL',
    oper_ip        varchar(128)  not null default '' comment '主机地址',
    oper_location  varchar(255)  not null default '' comment '操作地点',
    oper_param     varchar(2000) not null default '' comment '请求参数',
    json_result    varchar(2000) not null default '' comment '返回参数',
    status         int(1)        not null default 0 comment '操作状态（0正常 1异常）',
    error_msg      varchar(2000) not null default '' comment '错误消息',
    oper_time      datetime      not null comment '操作时间',
    primary key (id)
) engine = innodb
  auto_increment = 100 comment = '操作日志记录';
