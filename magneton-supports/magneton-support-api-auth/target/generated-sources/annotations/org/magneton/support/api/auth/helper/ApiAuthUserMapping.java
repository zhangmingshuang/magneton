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
import org.magneton.support.api.auth.entity.ApiAuthUserDO;
import org.magneton.support.api.auth.mapper.ApiAuthUserMapper;
import org.magneton.support.api.auth.wrapper.ApiAuthUserQuery;
import org.magneton.support.api.auth.wrapper.ApiAuthUserUpdate;

/**
 *
 * ApiAuthUserMapping: Entity帮助类
 *
 * @author powered by FluentMybatis
 */
public class ApiAuthUserMapping extends AMapping<ApiAuthUserDO, ApiAuthUserQuery, ApiAuthUserUpdate> {
  /**
   * 表名称
   */
  public static final String Table_Name = "api_auth_user";

  /**
   * Entity名称
   */
  public static final String Entity_Name = "ApiAuthUserDO";

  /**
   * 实体属性 : 数据库字段 映射
   *  id : id
   */
  public static final FieldMapping<ApiAuthUserDO> id = new FieldMapping<ApiAuthUserDO>
  	("id", "id", PRIMARY_ID, null, null, Integer.class, null)
  	.sg((e, v) -> e.setId((Integer) v), ApiAuthUserDO::getId);

  /**
   * 实体属性 : 数据库字段 映射
   *  account : account
   */
  public static final FieldMapping<ApiAuthUserDO> account = new FieldMapping<ApiAuthUserDO>
  	("account", "account", null, null, null, String.class, null)
  	.sg((e, v) -> e.setAccount((String) v), ApiAuthUserDO::getAccount);

  /**
   * 实体属性 : 数据库字段 映射
   *  additional : additional
   */
  public static final FieldMapping<ApiAuthUserDO> additional = new FieldMapping<ApiAuthUserDO>
  	("additional", "additional", null, null, null, String.class, null)
  	.sg((e, v) -> e.setAdditional((String) v), ApiAuthUserDO::getAdditional);

  /**
   * 实体属性 : 数据库字段 映射
   *  createAdditional : create_additional
   */
  public static final FieldMapping<ApiAuthUserDO> createAdditional = new FieldMapping<ApiAuthUserDO>
  	("createAdditional", "create_additional", null, null, null, String.class, null)
  	.sg((e, v) -> e.setCreateAdditional((String) v), ApiAuthUserDO::getCreateAdditional);

  /**
   * 实体属性 : 数据库字段 映射
   *  createTime : create_time
   */
  public static final FieldMapping<ApiAuthUserDO> createTime = new FieldMapping<ApiAuthUserDO>
  	("createTime", "create_time", null, null, null, Long.class, null)
  	.sg((e, v) -> e.setCreateTime((Long) v), ApiAuthUserDO::getCreateTime);

  /**
   * 实体属性 : 数据库字段 映射
   *  pwd : pwd
   */
  public static final FieldMapping<ApiAuthUserDO> pwd = new FieldMapping<ApiAuthUserDO>
  	("pwd", "pwd", null, null, null, String.class, null)
  	.sg((e, v) -> e.setPwd((String) v), ApiAuthUserDO::getPwd);

  /**
   * 实体属性 : 数据库字段 映射
   *  pwdSalt : pwd_salt
   */
  public static final FieldMapping<ApiAuthUserDO> pwdSalt = new FieldMapping<ApiAuthUserDO>
  	("pwdSalt", "pwd_salt", null, null, null, String.class, null)
  	.sg((e, v) -> e.setPwdSalt((String) v), ApiAuthUserDO::getPwdSalt);

  /**
   * 实体属性 : 数据库字段 映射
   *  removed : removed
   */
  public static final FieldMapping<ApiAuthUserDO> removed = new FieldMapping<ApiAuthUserDO>
  	("removed", "removed", null, null, null, Integer.class, null)
  	.sg((e, v) -> e.setRemoved((Integer) v), ApiAuthUserDO::getRemoved);

  /**
   * 实体属性 : 数据库字段 映射
   *  status : status
   */
  public static final FieldMapping<ApiAuthUserDO> status = new FieldMapping<ApiAuthUserDO>
  	("status", "status", null, null, null, Integer.class, null)
  	.sg((e, v) -> e.setStatus((Integer) v), ApiAuthUserDO::getStatus);

  public static final IDefaultSetter DEFAULT_SETTER = new IDefaultSetter(){};

  public static final List<FieldMapping> ALL_FIELD_MAPPING = Collections.unmodifiableList(Arrays
  	.asList(id, account, additional, createAdditional, createTime, pwd, pwdSalt, removed, status));

  public static final ApiAuthUserMapping MAPPING = new ApiAuthUserMapping();

  protected ApiAuthUserMapping() {
    super(DbType.MYSQL);
    super.tableName = Table_Name;
    super.tableId = new TableId("id", "id", true, "", false);
    super.uniqueFields.put(PRIMARY_ID, id);
    super.Ref_Keys.unmodified();
  }

  @Override
  public Class entityClass() {
    return ApiAuthUserDO.class;
  }

  @Override
  public Class mapperClass() {
    return ApiAuthUserMapper.class;
  }

  @Override
  public <E extends IEntity> E newEntity() {
    return (E) new ApiAuthUserDO();
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
  protected final ApiAuthUserQuery query(boolean defaults, StringSupplier table,
      StringSupplier alias, Parameters shared) {
    return new ApiAuthUserQuery(defaults, fragment(table), alias, shared);
  }

  @Override
  protected final ApiAuthUserUpdate updater(boolean defaults, StringSupplier table,
      StringSupplier alias, Parameters shared) {
    return new ApiAuthUserUpdate(defaults, fragment(table), alias, shared);
  }
}
