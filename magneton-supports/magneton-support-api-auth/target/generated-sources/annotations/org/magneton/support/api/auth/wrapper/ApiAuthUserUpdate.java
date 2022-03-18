package org.magneton.support.api.auth.wrapper;

import static cn.org.atool.fluent.mybatis.If.notBlank;
import static cn.org.atool.fluent.mybatis.segment.fragment.Fragments.fragment;
import static org.magneton.support.api.auth.helper.ApiAuthUserMapping.MAPPING;

import cn.org.atool.fluent.mybatis.base.crud.BaseUpdate;
import cn.org.atool.fluent.mybatis.base.entity.IMapping;
import cn.org.atool.fluent.mybatis.functions.StringSupplier;
import cn.org.atool.fluent.mybatis.segment.fragment.IFragment;
import cn.org.atool.fluent.mybatis.segment.model.Parameters;
import java.util.Optional;
import org.magneton.support.api.auth.entity.ApiAuthUserDO;
import org.magneton.support.api.auth.helper.ApiAuthUserSegment;

/**
 *
 * ApiAuthUserUpdate: 更新构造
 *
 * @author powered by FluentMybatis
 */
public class ApiAuthUserUpdate extends BaseUpdate<ApiAuthUserDO, ApiAuthUserUpdate, ApiAuthUserQuery> {
  public final ApiAuthUserSegment.UpdateSetter set = new ApiAuthUserSegment.UpdateSetter(this);

  public final ApiAuthUserSegment.UpdateWhere where = new ApiAuthUserSegment.UpdateWhere(this);

  public final ApiAuthUserSegment.UpdateOrderBy orderBy = new ApiAuthUserSegment.UpdateOrderBy(this);

  public ApiAuthUserUpdate() {
    this(true, null, null, null);
  }

  public ApiAuthUserUpdate(boolean defaults, IFragment table, StringSupplier alias,
      Parameters shared) {
    super(table, alias, ApiAuthUserDO.class);
    if(shared != null) {
      this.sharedParameter(shared);
    }
    if (defaults) {
      MAPPING.defaultSetter().setUpdateDefault(this);
    }
  }

  @Override
  public ApiAuthUserSegment.UpdateWhere where() {
    return this.where;
  }

  @Override
  public Optional<IMapping> mapping() {
    return Optional.of(MAPPING);
  }

  public static ApiAuthUserUpdate emptyUpdater() {
    return new ApiAuthUserUpdate(false, null, null, null);
  }

  public static ApiAuthUserUpdate emptyUpdater(StringSupplier table) {
    return new ApiAuthUserUpdate(false, fragment(table), null, null);
  }

  public static ApiAuthUserUpdate updater() {
    return new ApiAuthUserUpdate(true, null, null, null);
  }

  public static ApiAuthUserUpdate defaultUpdater() {
    return updater();
  }

  public static ApiAuthUserUpdate updater(StringSupplier table) {
    return new ApiAuthUserUpdate(true, fragment(table), null, null);
  }
}
