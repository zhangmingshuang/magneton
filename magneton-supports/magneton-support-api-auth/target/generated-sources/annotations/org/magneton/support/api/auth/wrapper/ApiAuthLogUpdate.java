package org.magneton.support.api.auth.wrapper;

import static cn.org.atool.fluent.mybatis.If.notBlank;
import static cn.org.atool.fluent.mybatis.segment.fragment.Fragments.fragment;
import static org.magneton.support.api.auth.helper.ApiAuthLogMapping.MAPPING;

import cn.org.atool.fluent.mybatis.base.crud.BaseUpdate;
import cn.org.atool.fluent.mybatis.base.entity.IMapping;
import cn.org.atool.fluent.mybatis.functions.StringSupplier;
import cn.org.atool.fluent.mybatis.segment.fragment.IFragment;
import cn.org.atool.fluent.mybatis.segment.model.Parameters;
import java.util.Optional;
import org.magneton.support.api.auth.entity.ApiAuthLogDO;
import org.magneton.support.api.auth.helper.ApiAuthLogSegment;

/**
 *
 * ApiAuthLogUpdate: 更新构造
 *
 * @author powered by FluentMybatis
 */
public class ApiAuthLogUpdate extends BaseUpdate<ApiAuthLogDO, ApiAuthLogUpdate, ApiAuthLogQuery> {
  public final ApiAuthLogSegment.UpdateSetter set = new ApiAuthLogSegment.UpdateSetter(this);

  public final ApiAuthLogSegment.UpdateWhere where = new ApiAuthLogSegment.UpdateWhere(this);

  public final ApiAuthLogSegment.UpdateOrderBy orderBy = new ApiAuthLogSegment.UpdateOrderBy(this);

  public ApiAuthLogUpdate() {
    this(true, null, null, null);
  }

  public ApiAuthLogUpdate(boolean defaults, IFragment table, StringSupplier alias,
      Parameters shared) {
    super(table, alias, ApiAuthLogDO.class);
    if(shared != null) {
      this.sharedParameter(shared);
    }
    if (defaults) {
      MAPPING.defaultSetter().setUpdateDefault(this);
    }
  }

  @Override
  public ApiAuthLogSegment.UpdateWhere where() {
    return this.where;
  }

  @Override
  public Optional<IMapping> mapping() {
    return Optional.of(MAPPING);
  }

  public static ApiAuthLogUpdate emptyUpdater() {
    return new ApiAuthLogUpdate(false, null, null, null);
  }

  public static ApiAuthLogUpdate emptyUpdater(StringSupplier table) {
    return new ApiAuthLogUpdate(false, fragment(table), null, null);
  }

  public static ApiAuthLogUpdate updater() {
    return new ApiAuthLogUpdate(true, null, null, null);
  }

  public static ApiAuthLogUpdate defaultUpdater() {
    return updater();
  }

  public static ApiAuthLogUpdate updater(StringSupplier table) {
    return new ApiAuthLogUpdate(true, fragment(table), null, null);
  }
}
