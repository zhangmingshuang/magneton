package org.magneton.support.admin.basic.wrapper;

import static cn.org.atool.fluent.mybatis.If.notBlank;
import static cn.org.atool.fluent.mybatis.segment.fragment.Fragments.fragment;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.assertNotNull;
import static cn.org.atool.fluent.mybatis.utility.StrConstant.EMPTY;
import static org.magneton.support.admin.basic.helper.AdminSysConfigMapping.MAPPING;

import cn.org.atool.fluent.mybatis.base.crud.BaseQuery;
import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.entity.IMapping;
import cn.org.atool.fluent.mybatis.functions.StringSupplier;
import cn.org.atool.fluent.mybatis.segment.fragment.BracketFrag;
import cn.org.atool.fluent.mybatis.segment.fragment.IFragment;
import cn.org.atool.fluent.mybatis.segment.model.Parameters;
import java.util.Optional;
import org.magneton.support.admin.basic.entity.AdminSysConfigDO;
import org.magneton.support.admin.basic.helper.AdminSysConfigSegment;

/**
 *
 * AdminSysConfigQuery: 查询构造
 *
 * @author powered by FluentMybatis
 */
public class AdminSysConfigQuery extends BaseQuery<AdminSysConfigDO, AdminSysConfigQuery> {
  /**
   * 指定查询字段, 默认无需设置
   */
  public final AdminSysConfigSegment.Selector select = new AdminSysConfigSegment.Selector(this);

  /**
   * 分组：GROUP BY 字段, ...
   * 例: groupBy('id', 'name')
   */
  public final AdminSysConfigSegment.GroupBy groupBy = new AdminSysConfigSegment.GroupBy(this);

  /**
   * 分组条件设置 having...
   */
  public final AdminSysConfigSegment.Having having = new AdminSysConfigSegment.Having(this);

  /**
   * 排序设置 order by ...
   */
  public final AdminSysConfigSegment.QueryOrderBy orderBy = new AdminSysConfigSegment.QueryOrderBy(this);

  /**
   * 查询条件 where ...
   */
  public final AdminSysConfigSegment.QueryWhere where = new AdminSysConfigSegment.QueryWhere(this);

  public AdminSysConfigQuery() {
    this(true, null, () -> null, null);
  }

  public AdminSysConfigQuery(String alias) {
    this(true, null, () -> alias, null);
  }

  public AdminSysConfigQuery(boolean defaults, IFragment table, String alias, Parameters shared) {
    this(defaults, table, () -> alias, shared);
  }

  public AdminSysConfigQuery(boolean defaults, IFragment table, StringSupplier alias,
      Parameters shared) {
    super(table, alias, AdminSysConfigDO.class);
    if(shared != null) {
      this.sharedParameter(shared);
    }
    if (defaults) {
      MAPPING.defaultSetter().setQueryDefault(this);
    }
  }

  @Override
  public AdminSysConfigSegment.QueryWhere where() {
    return this.where;
  }

  @Override
  public AdminSysConfigSegment.QueryOrderBy orderBy() {
    return this.orderBy;
  }

  @Override
  public Optional<IMapping> mapping() {
    return Optional.of(MAPPING);
  }

  public static AdminSysConfigQuery emptyQuery() {
    return new AdminSysConfigQuery(false, null, () -> null, null);
  }

  public static AdminSysConfigQuery emptyQuery(String alias) {
    return new AdminSysConfigQuery(false, null, () -> alias, null);
  }

  public static AdminSysConfigQuery emptyQuery(StringSupplier table) {
    return new AdminSysConfigQuery(false, fragment(table), () -> null, null);
  }

  public static AdminSysConfigQuery query() {
    return new AdminSysConfigQuery();
  }

  public static AdminSysConfigQuery defaultQuery() {
    return query();
  }

  /**
   * 显式指定表别名(join查询的时候需要定义表别名)
   */
  public static AdminSysConfigQuery query(String alias) {
    return new AdminSysConfigQuery(alias);
  }

  public static AdminSysConfigQuery query(StringSupplier table) {
    assertNotNull("table", table);
    return new AdminSysConfigQuery(true, fragment(table), () -> null, null);
  }

  public static AdminSysConfigQuery query(StringSupplier table, String alias) {
    assertNotNull("table", table);
    return new AdminSysConfigQuery(true, fragment(table), () -> alias, null);
  }

  /**
   * select * from (select query) alias
   * @param query 子查询
   * @param alias 子查询别名
   */
  public static AdminSysConfigQuery query(IQuery query, String alias) {
    assertNotNull("query", query);
    return new AdminSysConfigQuery(true, BracketFrag.set(query), () -> alias, null);
  }
}
