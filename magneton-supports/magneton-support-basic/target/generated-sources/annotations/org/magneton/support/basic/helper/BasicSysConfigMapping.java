package org.magneton.support.basic.helper;

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
import org.magneton.support.basic.entity.BasicSysConfigDO;
import org.magneton.support.basic.mapper.BasicSysConfigMapper;
import org.magneton.support.basic.wrapper.BasicSysConfigQuery;
import org.magneton.support.basic.wrapper.BasicSysConfigUpdate;

/**
 *
 * BasicSysConfigMapping: Entity帮助类
 *
 * @author powered by FluentMybatis
 */
public class BasicSysConfigMapping extends AMapping<BasicSysConfigDO, BasicSysConfigQuery, BasicSysConfigUpdate> {
  /**
   * 表名称
   */
  public static final String Table_Name = "basic_sys_config";

  /**
   * Entity名称
   */
  public static final String Entity_Name = "BasicSysConfigDO";

  /**
   * 实体属性 : 数据库字段 映射
   *  id : id
   */
  public static final FieldMapping<BasicSysConfigDO> id = new FieldMapping<BasicSysConfigDO>
  	("id", "id", PRIMARY_ID, null, null, Integer.class, null)
  	.sg((e, v) -> e.setId((Integer) v), BasicSysConfigDO::getId);

  /**
   * 实体属性 : 数据库字段 映射
   *  builtIn : built_in
   */
  public static final FieldMapping<BasicSysConfigDO> builtIn = new FieldMapping<BasicSysConfigDO>
  	("builtIn", "built_in", null, null, null, Boolean.class, null)
  	.sg((e, v) -> e.setBuiltIn((Boolean) v), BasicSysConfigDO::getBuiltIn);

  /**
   * 实体属性 : 数据库字段 映射
   *  configKey : config_key
   */
  public static final FieldMapping<BasicSysConfigDO> configKey = new FieldMapping<BasicSysConfigDO>
  	("configKey", "config_key", null, null, null, String.class, null)
  	.sg((e, v) -> e.setConfigKey((String) v), BasicSysConfigDO::getConfigKey);

  /**
   * 实体属性 : 数据库字段 映射
   *  configName : config_name
   */
  public static final FieldMapping<BasicSysConfigDO> configName = new FieldMapping<BasicSysConfigDO>
  	("configName", "config_name", null, null, null, String.class, null)
  	.sg((e, v) -> e.setConfigName((String) v), BasicSysConfigDO::getConfigName);

  /**
   * 实体属性 : 数据库字段 映射
   *  configValue : config_value
   */
  public static final FieldMapping<BasicSysConfigDO> configValue = new FieldMapping<BasicSysConfigDO>
  	("configValue", "config_value", null, null, null, String.class, null)
  	.sg((e, v) -> e.setConfigValue((String) v), BasicSysConfigDO::getConfigValue);

  /**
   * 实体属性 : 数据库字段 映射
   *  createBy : create_by
   */
  public static final FieldMapping<BasicSysConfigDO> createBy = new FieldMapping<BasicSysConfigDO>
  	("createBy", "create_by", null, null, null, String.class, null)
  	.sg((e, v) -> e.setCreateBy((String) v), BasicSysConfigDO::getCreateBy);

  /**
   * 实体属性 : 数据库字段 映射
   *  createTime : create_time
   */
  public static final FieldMapping<BasicSysConfigDO> createTime = new FieldMapping<BasicSysConfigDO>
  	("createTime", "create_time", null, null, null, Date.class, null)
  	.sg((e, v) -> e.setCreateTime((Date) v), BasicSysConfigDO::getCreateTime);

  /**
   * 实体属性 : 数据库字段 映射
   *  remark : remark
   */
  public static final FieldMapping<BasicSysConfigDO> remark = new FieldMapping<BasicSysConfigDO>
  	("remark", "remark", null, null, null, String.class, null)
  	.sg((e, v) -> e.setRemark((String) v), BasicSysConfigDO::getRemark);

  /**
   * 实体属性 : 数据库字段 映射
   *  updateBy : update_by
   */
  public static final FieldMapping<BasicSysConfigDO> updateBy = new FieldMapping<BasicSysConfigDO>
  	("updateBy", "update_by", null, null, null, String.class, null)
  	.sg((e, v) -> e.setUpdateBy((String) v), BasicSysConfigDO::getUpdateBy);

  /**
   * 实体属性 : 数据库字段 映射
   *  updateTime : update_time
   */
  public static final FieldMapping<BasicSysConfigDO> updateTime = new FieldMapping<BasicSysConfigDO>
  	("updateTime", "update_time", null, null, null, Date.class, null)
  	.sg((e, v) -> e.setUpdateTime((Date) v), BasicSysConfigDO::getUpdateTime);

  public static final IDefaultSetter DEFAULT_SETTER = new IDefaultSetter(){};

  public static final List<FieldMapping> ALL_FIELD_MAPPING = Collections.unmodifiableList(Arrays
  	.asList(id, builtIn, configKey, configName, configValue, createBy, createTime, remark, updateBy, updateTime));

  public static final BasicSysConfigMapping MAPPING = new BasicSysConfigMapping();

  protected BasicSysConfigMapping() {
    super(DbType.MYSQL);
    super.tableName = Table_Name;
    super.tableId = new TableId("id", "id", true, "", false);
    super.uniqueFields.put(PRIMARY_ID, id);
    super.Ref_Keys.unmodified();
  }

  @Override
  public Class entityClass() {
    return BasicSysConfigDO.class;
  }

  @Override
  public Class mapperClass() {
    return BasicSysConfigMapper.class;
  }

  @Override
  public <E extends IEntity> E newEntity() {
    return (E) new BasicSysConfigDO();
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
  protected final BasicSysConfigQuery query(boolean defaults, StringSupplier table,
      StringSupplier alias, Parameters shared) {
    return new BasicSysConfigQuery(defaults, fragment(table), alias, shared);
  }

  @Override
  protected final BasicSysConfigUpdate updater(boolean defaults, StringSupplier table,
      StringSupplier alias, Parameters shared) {
    return new BasicSysConfigUpdate(defaults, fragment(table), alias, shared);
  }
}
