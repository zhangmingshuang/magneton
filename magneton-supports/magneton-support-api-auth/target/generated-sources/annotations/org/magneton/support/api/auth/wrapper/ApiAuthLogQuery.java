package org.magneton.support.api.auth.wrapper;

import static cn.org.atool.fluent.mybatis.If.notBlank;
import static cn.org.atool.fluent.mybatis.segment.fragment.Fragments.fragment;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.assertNotNull;
import static cn.org.atool.fluent.mybatis.utility.StrConstant.EMPTY;
import static org.magneton.support.api.auth.helper.ApiAuthLogMapping.MAPPING;

import cn.org.atool.fluent.mybatis.base.crud.BaseQuery;
import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.entity.IMapping;
import cn.org.atool.fluent.mybatis.functions.StringSupplier;
import cn.org.atool.fluent.mybatis.segment.fragment.BracketFrag;
import cn.org.atool.fluent.mybatis.segment.fragment.IFragment;
import cn.org.atool.fluent.mybatis.segment.model.Parameters;
import java.util.Optional;
import org.magneton.support.api.auth.entity.ApiAuthLogDO;
import org.magneton.support.api.auth.helper.ApiAuthLogSegment;

/**
 *
 * ApiAuthLogQuery: 查询构造
 *
 * @author powered by FluentMybatis
 */
public class ApiAuthLogQuery extends BaseQuery<ApiAuthLogDO, ApiAuthLogQuery> {
  /**
   * 指定查询字段, 默认无需设置
   */
  public final ApiAuthLogSegment.Selector select = new ApiAuthLogSegment.Selector(this);

  /**
   * 分组：GROUP BY 字段, ...
   * 例: groupBy('id', 'name')
   */
  public final ApiAuthLogSegment.GroupBy groupBy = new ApiAuthLogSegment.GroupBy(this);

  /**
   * 分组条件设置 having...
   */
  public final ApiAuthLogSegment.Having having = new ApiAuthLogSegment.Having(this);

  /**
   * 排序设置 order by ...
   */
  public final ApiAuthLogSegment.QueryOrderBy orderBy = new ApiAuthLogSegment.QueryOrderBy(this);

  /**
   * 查询条件 where ...
   */
  public final ApiAuthLogSegment.QueryWhere where = new ApiAuthLogSegment.QueryWhere(this);

  public ApiAuthLogQuery() {
    this(true, null, () -> null, null);
  }

  public ApiAuthLogQuery(String alias) {
    this(true, null, () -> alias, null);
  }

  public ApiAuthLogQuery(boolean defaults, IFragment table, String alias, Parameters shared) {
    this(defaults, table, () -> alias, shared);
  }

  public ApiAuthLogQuery(boolean defaults, IFragment table, StringSupplier alias,
      Parameters shared) {
    super(table, alias, ApiAuthLogDO.class);
    if(shared != null) {
      this.sharedParameter(shared);
    }
    if (defaults) {
      MAPPING.defaultSetter().setQueryDefault(this);
    }
  }

  @Override
  public ApiAuthLogSegment.QueryWhere where() {
    return this.where;
  }

  @Override
  public ApiAuthLogSegment.QueryOrderBy orderBy() {
    return this.orderBy;
  }

  @Override
  public Optional<IMapping> mapping() {
    return Optional.of(MAPPING);
  }

  public static ApiAuthLogQuery emptyQuery() {
    return new ApiAuthLogQuery(false, null, () -> null, null);
  }

  public static ApiAuthLogQuery emptyQuery(String alias) {
    return new ApiAuthLogQuery(false, null, () -> alias, null);
  }

  public static ApiAuthLogQuery emptyQuery(StringSupplier table) {
    return new ApiAuthLogQuery(false, fragment(table), () -> null, null);
  }

  public static ApiAuthLogQuery query() {
    return new ApiAuthLogQuery();
  }

  public static ApiAuthLogQuery defaultQuery() {
    return query();
  }

  /**
   * 显式指定表别名(join查询的时候需要定义表别名)
   */
  public static ApiAuthLogQuery query(String alias) {
    return new ApiAuthLogQuery(alias);
  }

  public static ApiAuthLogQuery query(StringSupplier table) {
    assertNotNull("table", table);
    return new ApiAuthLogQuery(true, fragment(table), () -> null, null);
  }

  public static ApiAuthLogQuery query(StringSupplier table, String alias) {
    assertNotNull("table", table);
    return new ApiAuthLogQuery(true, fragment(table), () -> alias, null);
  }

  /**
   * select * from (select query) alias
   * @param query 子查询
   * @param alias 子查询别名
   */
  public static ApiAuthLogQuery query(IQuery query, String alias) {
    assertNotNull("query", query);
    return new ApiAuthLogQuery(true, BracketFrag.set(query), () -> alias, null);
  }
}
