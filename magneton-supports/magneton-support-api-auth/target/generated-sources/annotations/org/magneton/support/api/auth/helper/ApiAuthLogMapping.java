package org.magneton.support.api.auth.helper;

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
import java.util.List;
import org.magneton.support.api.auth.entity.ApiAuthLogDO;
import org.magneton.support.api.auth.mapper.ApiAuthLogMapper;
import org.magneton.support.api.auth.wrapper.ApiAuthLogQuery;
import org.magneton.support.api.auth.wrapper.ApiAuthLogUpdate;

/**
 *
 * ApiAuthLogMapping: Entity帮助类
 *
 * @author powered by FluentMybatis
 */
public class ApiAuthLogMapping extends AMapping<ApiAuthLogDO, ApiAuthLogQuery, ApiAuthLogUpdate> {
  /**
   * 表名称
   */
  public static final String Table_Name = "api_auth_log";

  /**
   * Entity名称
   */
  public static final String Entity_Name = "ApiAuthLogDO";

  /**
   * 实体属性 : 数据库字段 映射
   *  id : id
   */
  public static final FieldMapping<ApiAuthLogDO> id = new FieldMapping<ApiAuthLogDO>
  	("id", "id", PRIMARY_ID, null, null, Integer.class, null)
  	.sg((e, v) -> e.setId((Integer) v), ApiAuthLogDO::getId);

  /**
   * 实体属性 : 数据库字段 映射
   *  createAdditional : create_additional
   */
  public static final FieldMapping<ApiAuthLogDO> createAdditional = new FieldMapping<ApiAuthLogDO>
  	("createAdditional", "create_additional", null, null, null, String.class, null)
  	.sg((e, v) -> e.setCreateAdditional((String) v), ApiAuthLogDO::getCreateAdditional);

  /**
   * 实体属性 : 数据库字段 映射
   *  createTime : create_time
   */
  public static final FieldMapping<ApiAuthLogDO> createTime = new FieldMapping<ApiAuthLogDO>
  	("createTime", "create_time", null, null, null, Long.class, null)
  	.sg((e, v) -> e.setCreateTime((Long) v), ApiAuthLogDO::getCreateTime);

  /**
   * 实体属性 : 数据库字段 映射
   *  userId : user_id
   */
  public static final FieldMapping<ApiAuthLogDO> userId = new FieldMapping<ApiAuthLogDO>
  	("userId", "user_id", null, null, null, Integer.class, null)
  	.sg((e, v) -> e.setUserId((Integer) v), ApiAuthLogDO::getUserId);

  public static final IDefaultSetter DEFAULT_SETTER = new IDefaultSetter(){};

  public static final List<FieldMapping> ALL_FIELD_MAPPING = Collections.unmodifiableList(Arrays
  	.asList(id, createAdditional, createTime, userId));

  public static final ApiAuthLogMapping MAPPING = new ApiAuthLogMapping();

  protected ApiAuthLogMapping() {
    super(DbType.MYSQL);
    super.tableName = Table_Name;
    super.tableId = new TableId("id", "id", true, "", false);
    super.uniqueFields.put(PRIMARY_ID, id);
    super.Ref_Keys.unmodified();
  }

  @Override
  public Class entityClass() {
    return ApiAuthLogDO.class;
  }

  @Override
  public Class mapperClass() {
    return ApiAuthLogMapper.class;
  }

  @Override
  public <E extends IEntity> E newEntity() {
    return (E) new ApiAuthLogDO();
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
  protected final ApiAuthLogQuery query(boolean defaults, StringSupplier table,
      StringSupplier alias, Parameters shared) {
    return new ApiAuthLogQuery(defaults, fragment(table), alias, shared);
  }

  @Override
  protected final ApiAuthLogUpdate updater(boolean defaults, StringSupplier table,
      StringSupplier alias, Parameters shared) {
    return new ApiAuthLogUpdate(defaults, fragment(table), alias, shared);
  }
}
