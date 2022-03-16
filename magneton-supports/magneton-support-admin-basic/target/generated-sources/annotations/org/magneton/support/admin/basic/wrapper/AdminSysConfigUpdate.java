package org.magneton.support.admin.basic.wrapper;

import static cn.org.atool.fluent.mybatis.If.notBlank;
import static cn.org.atool.fluent.mybatis.segment.fragment.Fragments.fragment;
import static org.magneton.support.admin.basic.helper.AdminSysConfigMapping.MAPPING;

import cn.org.atool.fluent.mybatis.base.crud.BaseUpdate;
import cn.org.atool.fluent.mybatis.base.entity.IMapping;
import cn.org.atool.fluent.mybatis.functions.StringSupplier;
import cn.org.atool.fluent.mybatis.segment.fragment.IFragment;
import cn.org.atool.fluent.mybatis.segment.model.Parameters;
import java.util.Optional;
import org.magneton.support.admin.basic.entity.AdminSysConfigDO;
import org.magneton.support.admin.basic.helper.AdminSysConfigSegment;

/**
 *
 * AdminSysConfigUpdate: 更新构造
 *
 * @author powered by FluentMybatis
 */
public class AdminSysConfigUpdate extends BaseUpdate<AdminSysConfigDO, AdminSysConfigUpdate, AdminSysConfigQuery> {
  public final AdminSysConfigSegment.UpdateSetter set = new AdminSysConfigSegment.UpdateSetter(this);

  public final AdminSysConfigSegment.UpdateWhere where = new AdminSysConfigSegment.UpdateWhere(this);

  public final AdminSysConfigSegment.UpdateOrderBy orderBy = new AdminSysConfigSegment.UpdateOrderBy(this);

  public AdminSysConfigUpdate() {
    this(true, null, null, null);
  }

  public AdminSysConfigUpdate(boolean defaults, IFragment table, StringSupplier alias,
      Parameters shared) {
    super(table, alias, AdminSysConfigDO.class);
    if(shared != null) {
      this.sharedParameter(shared);
    }
    if (defaults) {
      MAPPING.defaultSetter().setUpdateDefault(this);
    }
  }

  @Override
  public AdminSysConfigSegment.UpdateWhere where() {
    return this.where;
  }

  @Override
  public Optional<IMapping> mapping() {
    return Optional.of(MAPPING);
  }

  public static AdminSysConfigUpdate emptyUpdater() {
    return new AdminSysConfigUpdate(false, null, null, null);
  }

  public static AdminSysConfigUpdate emptyUpdater(StringSupplier table) {
    return new AdminSysConfigUpdate(false, fragment(table), null, null);
  }

  public static AdminSysConfigUpdate updater() {
    return new AdminSysConfigUpdate(true, null, null, null);
  }

  public static AdminSysConfigUpdate defaultUpdater() {
    return updater();
  }

  public static AdminSysConfigUpdate updater(StringSupplier table) {
    return new AdminSysConfigUpdate(true, fragment(table), null, null);
  }
}
