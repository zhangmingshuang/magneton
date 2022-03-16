package org.magneton.support.api.auth.wrapper;

import static cn.org.atool.fluent.mybatis.If.notBlank;
import static cn.org.atool.fluent.mybatis.segment.fragment.Fragments.fragment;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.assertNotNull;
import static cn.org.atool.fluent.mybatis.utility.StrConstant.EMPTY;
import static org.magneton.support.api.auth.helper.ApiAuthStatisticsMapping.MAPPING;

import cn.org.atool.fluent.mybatis.base.crud.BaseQuery;
import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.entity.IMapping;
import cn.org.atool.fluent.mybatis.functions.StringSupplier;
import cn.org.atool.fluent.mybatis.segment.fragment.BracketFrag;
import cn.org.atool.fluent.mybatis.segment.fragment.IFragment;
import cn.org.atool.fluent.mybatis.segment.model.Parameters;
import java.util.Optional;
import org.magneton.support.api.auth.entity.ApiAuthStatisticsDO;
import org.magneton.support.api.auth.helper.ApiAuthStatisticsSegment;

/**
 *
 * ApiAuthStatisticsQuery: 查询构造
 *
 * @author powered by FluentMybatis
 */
public class ApiAuthStatisticsQuery extends BaseQuery<ApiAuthStatisticsDO, ApiAuthStatisticsQuery> {
  /**
   * 指定查询字段, 默认无需设置
   */
  public final ApiAuthStatisticsSegment.Selector select = new ApiAuthStatisticsSegment.Selector(this);

  /**
   * 分组：GROUP BY 字段, ...
   * 例: groupBy('id', 'name')
   */
  public final ApiAuthStatisticsSegment.GroupBy groupBy = new ApiAuthStatisticsSegment.GroupBy(this);

  /**
   * 分组条件设置 having...
   */
  public final ApiAuthStatisticsSegment.Having having = new ApiAuthStatisticsSegment.Having(this);

  /**
   * 排序设置 order by ...
   */
  public final ApiAuthStatisticsSegment.QueryOrderBy orderBy = new ApiAuthStatisticsSegment.QueryOrderBy(this);

  /**
   * 查询条件 where ...
   */
  public final ApiAuthStatisticsSegment.QueryWhere where = new ApiAuthStatisticsSegment.QueryWhere(this);

  public ApiAuthStatisticsQuery() {
    this(true, null, () -> null, null);
  }

  public ApiAuthStatisticsQuery(String alias) {
    this(true, null, () -> alias, null);
  }

  public ApiAuthStatisticsQuery(boolean defaults, IFragment table, String alias,
      Parameters shared) {
    this(defaults, table, () -> alias, shared);
  }

  public ApiAuthStatisticsQuery(boolean defaults, IFragment table, StringSupplier alias,
      Parameters shared) {
    super(table, alias, ApiAuthStatisticsDO.class);
    if(shared != null) {
      this.sharedParameter(shared);
    }
    if (defaults) {
      MAPPING.defaultSetter().setQueryDefault(this);
    }
  }

  @Override
  public ApiAuthStatisticsSegment.QueryWhere where() {
    return this.where;
  }

  @Override
  public ApiAuthStatisticsSegment.QueryOrderBy orderBy() {
    return this.orderBy;
  }

  @Override
  public Optional<IMapping> mapping() {
    return Optional.of(MAPPING);
  }

  public static ApiAuthStatisticsQuery emptyQuery() {
    return new ApiAuthStatisticsQuery(false, null, () -> null, null);
  }

  public static ApiAuthStatisticsQuery emptyQuery(String alias) {
    return new ApiAuthStatisticsQuery(false, null, () -> alias, null);
  }

  public static ApiAuthStatisticsQuery emptyQuery(StringSupplier table) {
    return new ApiAuthStatisticsQuery(false, fragment(table), () -> null, null);
  }

  public static ApiAuthStatisticsQuery query() {
    return new ApiAuthStatisticsQuery();
  }

  public static ApiAuthStatisticsQuery defaultQuery() {
    return query();
  }

  /**
   * 显式指定表别名(join查询的时候需要定义表别名)
   */
  public static ApiAuthStatisticsQuery query(String alias) {
    return new ApiAuthStatisticsQuery(alias);
  }

  public static ApiAuthStatisticsQuery query(StringSupplier table) {
    assertNotNull("table", table);
    return new ApiAuthStatisticsQuery(true, fragment(table), () -> null, null);
  }

  public static ApiAuthStatisticsQuery query(StringSupplier table, String alias) {
    assertNotNull("table", table);
    return new ApiAuthStatisticsQuery(true, fragment(table), () -> alias, null);
  }

  /**
   * select * from (select query) alias
   * @param query 子查询
   * @param alias 子查询别名
   */
  public static ApiAuthStatisticsQuery query(IQuery query, String alias) {
    assertNotNull("query", query);
    return new ApiAuthStatisticsQuery(true, BracketFrag.set(query), () -> alias, null);
  }
}
