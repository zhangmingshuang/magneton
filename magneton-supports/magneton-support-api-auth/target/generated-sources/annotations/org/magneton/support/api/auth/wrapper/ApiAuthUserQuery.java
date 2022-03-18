package org.magneton.support.api.auth.wrapper;

import static cn.org.atool.fluent.mybatis.If.notBlank;
import static cn.org.atool.fluent.mybatis.segment.fragment.Fragments.fragment;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.assertNotNull;
import static cn.org.atool.fluent.mybatis.utility.StrConstant.EMPTY;
import static org.magneton.support.api.auth.helper.ApiAuthUserMapping.MAPPING;

import cn.org.atool.fluent.mybatis.base.crud.BaseQuery;
import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.entity.IMapping;
import cn.org.atool.fluent.mybatis.functions.StringSupplier;
import cn.org.atool.fluent.mybatis.segment.fragment.BracketFrag;
import cn.org.atool.fluent.mybatis.segment.fragment.IFragment;
import cn.org.atool.fluent.mybatis.segment.model.Parameters;
import java.util.Optional;
import org.magneton.support.api.auth.entity.ApiAuthUserDO;
import org.magneton.support.api.auth.helper.ApiAuthUserSegment;

/**
 *
 * ApiAuthUserQuery: 查询构造
 *
 * @author powered by FluentMybatis
 */
public class ApiAuthUserQuery extends BaseQuery<ApiAuthUserDO, ApiAuthUserQuery> {
  /**
   * 指定查询字段, 默认无需设置
   */
  public final ApiAuthUserSegment.Selector select = new ApiAuthUserSegment.Selector(this);

  /**
   * 分组：GROUP BY 字段, ...
   * 例: groupBy('id', 'name')
   */
  public final ApiAuthUserSegment.GroupBy groupBy = new ApiAuthUserSegment.GroupBy(this);

  /**
   * 分组条件设置 having...
   */
  public final ApiAuthUserSegment.Having having = new ApiAuthUserSegment.Having(this);

  /**
   * 排序设置 order by ...
   */
  public final ApiAuthUserSegment.QueryOrderBy orderBy = new ApiAuthUserSegment.QueryOrderBy(this);

  /**
   * 查询条件 where ...
   */
  public final ApiAuthUserSegment.QueryWhere where = new ApiAuthUserSegment.QueryWhere(this);

  public ApiAuthUserQuery() {
    this(true, null, () -> null, null);
  }

  public ApiAuthUserQuery(String alias) {
    this(true, null, () -> alias, null);
  }

  public ApiAuthUserQuery(boolean defaults, IFragment table, String alias, Parameters shared) {
    this(defaults, table, () -> alias, shared);
  }

  public ApiAuthUserQuery(boolean defaults, IFragment table, StringSupplier alias,
      Parameters shared) {
    super(table, alias, ApiAuthUserDO.class);
    if(shared != null) {
      this.sharedParameter(shared);
    }
    if (defaults) {
      MAPPING.defaultSetter().setQueryDefault(this);
    }
  }

  @Override
  public ApiAuthUserSegment.QueryWhere where() {
    return this.where;
  }

  @Override
  public ApiAuthUserSegment.QueryOrderBy orderBy() {
    return this.orderBy;
  }

  @Override
  public Optional<IMapping> mapping() {
    return Optional.of(MAPPING);
  }

  public static ApiAuthUserQuery emptyQuery() {
    return new ApiAuthUserQuery(false, null, () -> null, null);
  }

  public static ApiAuthUserQuery emptyQuery(String alias) {
    return new ApiAuthUserQuery(false, null, () -> alias, null);
  }

  public static ApiAuthUserQuery emptyQuery(StringSupplier table) {
    return new ApiAuthUserQuery(false, fragment(table), () -> null, null);
  }

  public static ApiAuthUserQuery query() {
    return new ApiAuthUserQuery();
  }

  public static ApiAuthUserQuery defaultQuery() {
    return query();
  }

  /**
   * 显式指定表别名(join查询的时候需要定义表别名)
   */
  public static ApiAuthUserQuery query(String alias) {
    return new ApiAuthUserQuery(alias);
  }

  public static ApiAuthUserQuery query(StringSupplier table) {
    assertNotNull("table", table);
    return new ApiAuthUserQuery(true, fragment(table), () -> null, null);
  }

  public static ApiAuthUserQuery query(StringSupplier table, String alias) {
    assertNotNull("table", table);
    return new ApiAuthUserQuery(true, fragment(table), () -> alias, null);
  }

  /**
   * select * from (select query) alias
   * @param query 子查询
   * @param alias 子查询别名
   */
  public static ApiAuthUserQuery query(IQuery query, String alias) {
    assertNotNull("query", query);
    return new ApiAuthUserQuery(true, BracketFrag.set(query), () -> alias, null);
  }
}
