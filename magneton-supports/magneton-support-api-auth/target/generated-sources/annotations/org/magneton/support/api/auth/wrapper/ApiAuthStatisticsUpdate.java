package org.magneton.support.api.auth.wrapper;

import static cn.org.atool.fluent.mybatis.If.notBlank;
import static cn.org.atool.fluent.mybatis.segment.fragment.Fragments.fragment;
import static org.magneton.support.api.auth.helper.ApiAuthStatisticsMapping.MAPPING;

import cn.org.atool.fluent.mybatis.base.crud.BaseUpdate;
import cn.org.atool.fluent.mybatis.base.entity.IMapping;
import cn.org.atool.fluent.mybatis.functions.StringSupplier;
import cn.org.atool.fluent.mybatis.segment.fragment.IFragment;
import cn.org.atool.fluent.mybatis.segment.model.Parameters;
import java.util.Optional;
import org.magneton.support.api.auth.entity.ApiAuthStatisticsDO;
import org.magneton.support.api.auth.helper.ApiAuthStatisticsSegment;

/**
 *
 * ApiAuthStatisticsUpdate: 更新构造
 *
 * @author powered by FluentMybatis
 */
public class ApiAuthStatisticsUpdate extends BaseUpdate<ApiAuthStatisticsDO, ApiAuthStatisticsUpdate, ApiAuthStatisticsQuery> {
  public final ApiAuthStatisticsSegment.UpdateSetter set = new ApiAuthStatisticsSegment.UpdateSetter(this);

  public final ApiAuthStatisticsSegment.UpdateWhere where = new ApiAuthStatisticsSegment.UpdateWhere(this);

  public final ApiAuthStatisticsSegment.UpdateOrderBy orderBy = new ApiAuthStatisticsSegment.UpdateOrderBy(this);

  public ApiAuthStatisticsUpdate() {
    this(true, null, null, null);
  }

  public ApiAuthStatisticsUpdate(boolean defaults, IFragment table, StringSupplier alias,
      Parameters shared) {
    super(table, alias, ApiAuthStatisticsDO.class);
    if(shared != null) {
      this.sharedParameter(shared);
    }
    if (defaults) {
      MAPPING.defaultSetter().setUpdateDefault(this);
    }
  }

  @Override
  public ApiAuthStatisticsSegment.UpdateWhere where() {
    return this.where;
  }

  @Override
  public Optional<IMapping> mapping() {
    return Optional.of(MAPPING);
  }

  public static ApiAuthStatisticsUpdate emptyUpdater() {
    return new ApiAuthStatisticsUpdate(false, null, null, null);
  }

  public static ApiAuthStatisticsUpdate emptyUpdater(StringSupplier table) {
    return new ApiAuthStatisticsUpdate(false, fragment(table), null, null);
  }

  public static ApiAuthStatisticsUpdate updater() {
    return new ApiAuthStatisticsUpdate(true, null, null, null);
  }

  public static ApiAuthStatisticsUpdate defaultUpdater() {
    return updater();
  }

  public static ApiAuthStatisticsUpdate updater(StringSupplier table) {
    return new ApiAuthStatisticsUpdate(true, fragment(table), null, null);
  }
}
