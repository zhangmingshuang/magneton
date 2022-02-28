package org.magneton.support.basic.wrapper;

import static cn.org.atool.fluent.mybatis.If.notBlank;
import static cn.org.atool.fluent.mybatis.segment.fragment.Fragments.fragment;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.assertNotNull;
import static cn.org.atool.fluent.mybatis.utility.StrConstant.EMPTY;
import static org.magneton.support.basic.helper.BasicSysConfigMapping.MAPPING;

import cn.org.atool.fluent.mybatis.base.crud.BaseQuery;
import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.entity.IMapping;
import cn.org.atool.fluent.mybatis.functions.StringSupplier;
import cn.org.atool.fluent.mybatis.segment.fragment.BracketFrag;
import cn.org.atool.fluent.mybatis.segment.fragment.IFragment;
import cn.org.atool.fluent.mybatis.segment.model.Parameters;
import java.util.Optional;
import org.magneton.support.basic.entity.BasicSysConfigDO;
import org.magneton.support.basic.helper.BasicSysConfigSegment;

/**
 *
 * BasicSysConfigQuery: 查询构造
 *
 * @author powered by FluentMybatis
 */
public class BasicSysConfigQuery extends BaseQuery<BasicSysConfigDO, BasicSysConfigQuery> {
  /**
   * 指定查询字段, 默认无需设置
   */
  public final BasicSysConfigSegment.Selector select = new BasicSysConfigSegment.Selector(this);

  /**
   * 分组：GROUP BY 字段, ...
   * 例: groupBy('id', 'name')
   */
  public final BasicSysConfigSegment.GroupBy groupBy = new BasicSysConfigSegment.GroupBy(this);

  /**
   * 分组条件设置 having...
   */
  public final BasicSysConfigSegment.Having having = new BasicSysConfigSegment.Having(this);

  /**
   * 排序设置 order by ...
   */
  public final BasicSysConfigSegment.QueryOrderBy orderBy = new BasicSysConfigSegment.QueryOrderBy(this);

  /**
   * 查询条件 where ...
   */
  public final BasicSysConfigSegment.QueryWhere where = new BasicSysConfigSegment.QueryWhere(this);

  public BasicSysConfigQuery() {
    this(true, null, () -> null, null);
  }

  public BasicSysConfigQuery(String alias) {
    this(true, null, () -> alias, null);
  }

  public BasicSysConfigQuery(boolean defaults, IFragment table, String alias, Parameters shared) {
    this(defaults, table, () -> alias, shared);
  }

  public BasicSysConfigQuery(boolean defaults, IFragment table, StringSupplier alias,
      Parameters shared) {
    super(table, alias, BasicSysConfigDO.class);
    if(shared != null) {
      this.sharedParameter(shared);
    }
    if (defaults) {
      MAPPING.defaultSetter().setQueryDefault(this);
    }
  }

  @Override
  public BasicSysConfigSegment.QueryWhere where() {
    return this.where;
  }

  @Override
  public BasicSysConfigSegment.QueryOrderBy orderBy() {
    return this.orderBy;
  }

  @Override
  public Optional<IMapping> mapping() {
    return Optional.of(MAPPING);
  }

  public static BasicSysConfigQuery emptyQuery() {
    return new BasicSysConfigQuery(false, null, () -> null, null);
  }

  public static BasicSysConfigQuery emptyQuery(String alias) {
    return new BasicSysConfigQuery(false, null, () -> alias, null);
  }

  public static BasicSysConfigQuery emptyQuery(StringSupplier table) {
    return new BasicSysConfigQuery(false, fragment(table), () -> null, null);
  }

  public static BasicSysConfigQuery query() {
    return new BasicSysConfigQuery();
  }

  public static BasicSysConfigQuery defaultQuery() {
    return query();
  }

  /**
   * 显式指定表别名(join查询的时候需要定义表别名)
   */
  public static BasicSysConfigQuery query(String alias) {
    return new BasicSysConfigQuery(alias);
  }

  public static BasicSysConfigQuery query(StringSupplier table) {
    assertNotNull("table", table);
    return new BasicSysConfigQuery(true, fragment(table), () -> null, null);
  }

  public static BasicSysConfigQuery query(StringSupplier table, String alias) {
    assertNotNull("table", table);
    return new BasicSysConfigQuery(true, fragment(table), () -> alias, null);
  }

  /**
   * select * from (select query) alias
   * @param query 子查询
   * @param alias 子查询别名
   */
  public static BasicSysConfigQuery query(IQuery query, String alias) {
    assertNotNull("query", query);
    return new BasicSysConfigQuery(true, BracketFrag.set(query), () -> alias, null);
  }
}
