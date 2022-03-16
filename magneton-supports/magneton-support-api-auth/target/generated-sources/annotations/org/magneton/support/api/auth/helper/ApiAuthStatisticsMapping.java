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
import org.magneton.support.api.auth.entity.ApiAuthStatisticsDO;
import org.magneton.support.api.auth.mapper.ApiAuthStatisticsMapper;
import org.magneton.support.api.auth.wrapper.ApiAuthStatisticsQuery;
import org.magneton.support.api.auth.wrapper.ApiAuthStatisticsUpdate;

/**
 *
 * ApiAuthStatisticsMapping: Entity帮助类
 *
 * @author powered by FluentMybatis
 */
public class ApiAuthStatisticsMapping extends AMapping<ApiAuthStatisticsDO, ApiAuthStatisticsQuery, ApiAuthStatisticsUpdate> {
  /**
   * 表名称
   */
  public static final String Table_Name = "api_auth_statistics";

  /**
   * Entity名称
   */
  public static final String Entity_Name = "ApiAuthStatisticsDO";

  /**
   * 实体属性 : 数据库字段 映射
   *  id : id
   */
  public static final FieldMapping<ApiAuthStatisticsDO> id = new FieldMapping<ApiAuthStatisticsDO>
  	("id", "id", PRIMARY_ID, null, null, Integer.class, null)
  	.sg((e, v) -> e.setId((Integer) v), ApiAuthStatisticsDO::getId);

  /**
   * 实体属性 : 数据库字段 映射
   *  pv : pv
   */
  public static final FieldMapping<ApiAuthStatisticsDO> pv = new FieldMapping<ApiAuthStatisticsDO>
  	("pv", "pv", null, null, null, Integer.class, null)
  	.sg((e, v) -> e.setPv((Integer) v), ApiAuthStatisticsDO::getPv);

  /**
   * 实体属性 : 数据库字段 映射
   *  today : today
   */
  public static final FieldMapping<ApiAuthStatisticsDO> today = new FieldMapping<ApiAuthStatisticsDO>
  	("today", "today", null, null, null, Integer.class, null)
  	.sg((e, v) -> e.setToday((Integer) v), ApiAuthStatisticsDO::getToday);

  /**
   * 实体属性 : 数据库字段 映射
   *  uv : uv
   */
  public static final FieldMapping<ApiAuthStatisticsDO> uv = new FieldMapping<ApiAuthStatisticsDO>
  	("uv", "uv", null, null, null, Integer.class, null)
  	.sg((e, v) -> e.setUv((Integer) v), ApiAuthStatisticsDO::getUv);

  public static final IDefaultSetter DEFAULT_SETTER = new IDefaultSetter(){};

  public static final List<FieldMapping> ALL_FIELD_MAPPING = Collections.unmodifiableList(Arrays
  	.asList(id, pv, today, uv));

  public static final ApiAuthStatisticsMapping MAPPING = new ApiAuthStatisticsMapping();

  protected ApiAuthStatisticsMapping() {
    super(DbType.MYSQL);
    super.tableName = Table_Name;
    super.tableId = new TableId("id", "id", true, "", false);
    super.uniqueFields.put(PRIMARY_ID, id);
    super.Ref_Keys.unmodified();
  }

  @Override
  public Class entityClass() {
    return ApiAuthStatisticsDO.class;
  }

  @Override
  public Class mapperClass() {
    return ApiAuthStatisticsMapper.class;
  }

  @Override
  public <E extends IEntity> E newEntity() {
    return (E) new ApiAuthStatisticsDO();
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
  protected final ApiAuthStatisticsQuery query(boolean defaults, StringSupplier table,
      StringSupplier alias, Parameters shared) {
    return new ApiAuthStatisticsQuery(defaults, fragment(table), alias, shared);
  }

  @Override
  protected final ApiAuthStatisticsUpdate updater(boolean defaults, StringSupplier table,
      StringSupplier alias, Parameters shared) {
    return new ApiAuthStatisticsUpdate(defaults, fragment(table), alias, shared);
  }
}
