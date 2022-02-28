package org.magneton.support.basic.entity;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.annotation.TableField;
import cn.org.atool.fluent.mybatis.annotation.TableId;
import cn.org.atool.fluent.mybatis.base.RichEntity;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * BasicSysConfigDO: 数据映射实体定义
 *
 * @author Powered By Fluent Mybatis
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@Data
@Accessors(
    chain = true
)
@EqualsAndHashCode(
    callSuper = false
)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FluentMybatis(
    table = "basic_sys_config",
    schema = "magneton",
    suffix = "DO"
)
public class BasicSysConfigDO extends RichEntity {
  private static final long serialVersionUID = 1L;

  @TableId(
      value = "id",
      desc = "主键"
  )
  private Integer id;

  @TableField(
      value = "built_in",
      desc = "系统内置（Y是 N否）"
  )
  private Boolean builtIn;

  @TableField(
      value = "config_key",
      desc = "参数键名"
  )
  private String configKey;

  @TableField(
      value = "config_name",
      desc = "参数名称"
  )
  private String configName;

  @TableField(
      value = "config_value",
      desc = "参数键值"
  )
  private String configValue;

  @TableField(
      value = "create_by",
      desc = "创建者"
  )
  private String createBy;

  @TableField(
      value = "create_time",
      desc = "创建时间"
  )
  private Date createTime;

  @TableField(
      value = "remark",
      desc = "备注"
  )
  private String remark;

  @TableField(
      value = "update_by",
      desc = "更新者"
  )
  private String updateBy;

  @TableField(
      value = "update_time",
      desc = "更新时间"
  )
  private Date updateTime;

  @Override
  public final Class entityClass() {
    return BasicSysConfigDO.class;
  }
}
