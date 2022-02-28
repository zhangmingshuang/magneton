package org.magneton.support.basic.wrapper;

import static cn.org.atool.fluent.mybatis.If.notBlank;
import static cn.org.atool.fluent.mybatis.segment.fragment.Fragments.fragment;
import static org.magneton.support.basic.helper.BasicSysConfigMapping.MAPPING;

import cn.org.atool.fluent.mybatis.base.crud.BaseUpdate;
import cn.org.atool.fluent.mybatis.base.entity.IMapping;
import cn.org.atool.fluent.mybatis.functions.StringSupplier;
import cn.org.atool.fluent.mybatis.segment.fragment.IFragment;
import cn.org.atool.fluent.mybatis.segment.model.Parameters;
import java.util.Optional;
import org.magneton.support.basic.entity.BasicSysConfigDO;
import org.magneton.support.basic.helper.BasicSysConfigSegment;

/**
 *
 * BasicSysConfigUpdate: 更新构造
 *
 * @author powered by FluentMybatis
 */
public class BasicSysConfigUpdate extends BaseUpdate<BasicSysConfigDO, BasicSysConfigUpdate, BasicSysConfigQuery> {
  public final BasicSysConfigSegment.UpdateSetter set = new BasicSysConfigSegment.UpdateSetter(this);

  public final BasicSysConfigSegment.UpdateWhere where = new BasicSysConfigSegment.UpdateWhere(this);

  public final BasicSysConfigSegment.UpdateOrderBy orderBy = new BasicSysConfigSegment.UpdateOrderBy(this);

  public BasicSysConfigUpdate() {
    this(true, null, null, null);
  }

  public BasicSysConfigUpdate(boolean defaults, IFragment table, StringSupplier alias,
      Parameters shared) {
    super(table, alias, BasicSysConfigDO.class);
    if(shared != null) {
      this.sharedParameter(shared);
    }
    if (defaults) {
      MAPPING.defaultSetter().setUpdateDefault(this);
    }
  }

  @Override
  public BasicSysConfigSegment.UpdateWhere where() {
    return this.where;
  }

  @Override
  public Optional<IMapping> mapping() {
    return Optional.of(MAPPING);
  }

  public static BasicSysConfigUpdate emptyUpdater() {
    return new BasicSysConfigUpdate(false, null, null, null);
  }

  public static BasicSysConfigUpdate emptyUpdater(StringSupplier table) {
    return new BasicSysConfigUpdate(false, fragment(table), null, null);
  }

  public static BasicSysConfigUpdate updater() {
    return new BasicSysConfigUpdate(true, null, null, null);
  }

  public static BasicSysConfigUpdate defaultUpdater() {
    return updater();
  }

  public static BasicSysConfigUpdate updater(StringSupplier table) {
    return new BasicSysConfigUpdate(true, fragment(table), null, null);
  }
}
