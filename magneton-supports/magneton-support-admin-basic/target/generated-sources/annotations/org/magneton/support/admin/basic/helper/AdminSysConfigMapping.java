package org.magneton.support.admin.basic.helper;

import static cn.org.atool.fluent.mybatis.base.model.UniqueType.*;
import static cn.org.atool.fluent.mybatis.segment.fragment.Fragments.fragment;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.crud.IDefaultSetter;
import cn.org.atool.fluent.mybatis.base.entity.AMapping;
import cn.org.atool.fluent.mybatis.base.entity.TableId;
import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import cn.org.atool.fluent.mybatis.functions.StringSupplier;
import cn.org.atool.fluent.mybatis.metadata.DbType;
import cn.org.atool.fluent.mybatis.segment.model.Parameters;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.magneton.support.admin.basic.entity.AdminSysConfigDO;
import org.magneton.support.admin.basic.mapper.AdminSysConfigMapper;
import org.magneton.support.admin.basic.wrapper.AdminSysConfigQuery;
import org.magneton.support.admin.basic.wrapper.AdminSysConfigUpdate;

/**
 *
 * AdminSysConfigMapping: Entity帮助类
 *
 * @author powered by FluentMybatis
 */
public class AdminSysConfigMapping extends AMapping<AdminSysConfigDO, AdminSysConfigQuery, AdminSysConfigUpdate> {
  /**
   * 表名称
   */
  public static final String Table_Name = "admin_sys_config";

  /**
   * Entity名称
   */
  public static final String Entity_Name = "AdminSysConfigDO";

  /**
   * 实体属性 : 数据库字段 映射
   *  id : id
   */
  public static final FieldMapping<AdminSysConfigDO> id = new FieldMapping<AdminSysConfigDO>
  	("id", "id", PRIMARY_ID, null, null, Integer.class, null)
  	.sg((e, v) -> e.setId((Integer) v), AdminSysConfigDO::getId);

  /**
   * 实体属性 : 数据库字段 映射
   *  builtIn : built_in
   */
  public static final FieldMapping<AdminSysConfigDO> builtIn = new FieldMapping<AdminSysConfigDO>
  	("builtIn", "built_in", null, null, null, Boolean.class, null)
  	.sg((e, v) -> e.setBuiltIn((Boolean) v), AdminSysConfigDO::getBuiltIn);

  /**
   * 实体属性 : 数据库字段 映射
   *  configKey : config_key
   */
  public static final FieldMapping<AdminSysConfigDO> configKey = new FieldMapping<AdminSysConfigDO>
  	("configKey", "config_key", null, null, null, String.class, null)
  	.sg((e, v) -> e.setConfigKey((String) v), AdminSysConfigDO::getConfigKey);

  /**
   * 实体属性 : 数据库字段 映射
   *  configName : config_name
   */
  public static final FieldMapping<AdminSysConfigDO> configName = new FieldMapping<AdminSysConfigDO>
  	("configName", "config_name", null, null, null, String.class, null)
  	.sg((e, v) -> e.setConfigName((String) v), AdminSysConfigDO::getConfigName);

  /**
   * 实体属性 : 数据库字段 映射
   *  configValue : config_value
   */
  public static final FieldMapping<AdminSysConfigDO> configValue = new FieldMapping<AdminSysConfigDO>
  	("configValue", "config_value", null, null, null, String.class, null)
  	.sg((e, v) -> e.setConfigValue((String) v), AdminSysConfigDO::getConfigValue);

  /**
   * 实体属性 : 数据库字段 映射
   *  createBy : create_by
   */
  public static final FieldMapping<AdminSysConfigDO> createBy = new FieldMapping<AdminSysConfigDO>
  	("createBy", "create_by", null, null, null, String.class, null)
  	.sg((e, v) -> e.setCreateBy((String) v), AdminSysConfigDO::getCreateBy);

  /**
   * 实体属性 : 数据库字段 映射
   *  createTime : create_time
   */
  public static final FieldMapping<AdminSysConfigDO> createTime = new FieldMapping<AdminSysConfigDO>
  	("createTime", "create_time", null, null, null, Date.class, null)
  	.sg((e, v) -> e.setCreateTime((Date) v), AdminSysConfigDO::getCreateTime);

  /**
   * 实体属性 : 数据库字段 映射
   *  remark : remark
   */
  public static final FieldMapping<AdminSysConfigDO> remark = new FieldMapping<AdminSysConfigDO>
  	("remark", "remark", null, null, null, String.class, null)
  	.sg((e, v) -> e.setRemark((String) v), AdminSysConfigDO::getRemark);

  /**
   * 实体属性 : 数据库字段 映射
   *  updateBy : update_by
   */
  public static final FieldMapping<AdminSysConfigDO> updateBy = new FieldMapping<AdminSysConfigDO>
  	("updateBy", "update_by", null, null, null, String.class, null)
  	.sg((e, v) -> e.setUpdateBy((String) v), AdminSysConfigDO::getUpdateBy);

  /**
   * 实体属性 : 数据库字段 映射
   *  updateTime : update_time
   */
  public static final FieldMapping<AdminSysConfigDO> updateTime = new FieldMapping<AdminSysConfigDO>
  	("updateTime", "update_time", null, null, null, Date.class, null)
  	.sg((e, v) -> e.setUpdateTime((Date) v), AdminSysConfigDO::getUpdateTime);

  public static final IDefaultSetter DEFAULT_SETTER = new IDefaultSetter(){};

  public static final List<FieldMapping> ALL_FIELD_MAPPING = Collections.unmodifiableList(Arrays
  	.asList(id, builtIn, configKey, configName, configValue, createBy, createTime, remark, updateBy, updateTime));

  public static final AdminSysConfigMapping MAPPING = new AdminSysConfigMapping();

  protected AdminSysConfigMapping() {
    super(DbType.MYSQL);
    super.tableName = Table_Name;
    super.tableId = new TableId("id", "id", true, "", false);
    super.uniqueFields.put(PRIMARY_ID, id);
    super.Ref_Keys.unmodified();
  }

  @Override
  public Class entityClass() {
    return AdminSysConfigDO.class;
  }

  @Override
  public Class mapperClass() {
    return AdminSysConfigMapper.class;
  }

  @Override
  public <E extends IEntity> E newEntity() {
    return (E) new AdminSysConfigDO();
  }

  @Override
  public final List<FieldMapping> allFields() {
    return ALL_FIELD_MAPPING;
  }

  @Override
  public IDefaultSetter defaultSetter() {
    return DEFAULT_SETTER;
  }

  @Override
  protected final AdminSysConfigQuery query(boolean defaults, StringSupplier table,
      StringSupplier alias, Parameters shared) {
    return new AdminSysConfigQuery(defaults, fragment(table), alias, shared);
  }

  @Override
  protected final AdminSysConfigUpdate updater(boolean defaults, StringSupplier table,
      StringSupplier alias, Parameters shared) {
    return new AdminSysConfigUpdate(defaults, fragment(table), alias, shared);
  }
}
