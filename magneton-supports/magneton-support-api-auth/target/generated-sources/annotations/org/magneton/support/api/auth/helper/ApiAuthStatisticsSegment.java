package org.magneton.support.api.auth.helper;

import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.assertNotNull;
import static org.magneton.support.api.auth.helper.ApiAuthStatisticsMapping.*;

import cn.org.atool.fluent.mybatis.base.crud.IWrapper;
import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import cn.org.atool.fluent.mybatis.functions.IAggregate;
import cn.org.atool.fluent.mybatis.segment.GroupByBase;
import cn.org.atool.fluent.mybatis.segment.HavingBase;
import cn.org.atool.fluent.mybatis.segment.HavingOperator;
import cn.org.atool.fluent.mybatis.segment.OrderByApply;
import cn.org.atool.fluent.mybatis.segment.OrderByBase;
import cn.org.atool.fluent.mybatis.segment.SelectorBase;
import cn.org.atool.fluent.mybatis.segment.UpdateApply;
import cn.org.atool.fluent.mybatis.segment.UpdateBase;
import cn.org.atool.fluent.mybatis.segment.WhereBase;
import cn.org.atool.fluent.mybatis.segment.where.NumericWhere;
import org.magneton.support.api.auth.wrapper.ApiAuthStatisticsQuery;
import org.magneton.support.api.auth.wrapper.ApiAuthStatisticsUpdate;

/**
 *
 * ApiAuthStatisticsSegment
 *
 * @author powered by FluentMybatis
 */
@SuppressWarnings({"unused", "rawtypes", "unchecked"})
public interface ApiAuthStatisticsSegment {
  interface ASegment<R> {
    R set(FieldMapping fieldMapping);

    default R id() {
      return this.set(id);
    }

    default R pv() {
      return this.set(pv);
    }

    default R today() {
      return this.set(today);
    }

    default R uv() {
      return this.set(uv);
    }
  }

  /**
   * select字段设置
   */
  final class Selector extends SelectorBase<Selector, ApiAuthStatisticsQuery> implements ASegment<Selector> {
    public Selector(ApiAuthStatisticsQuery query) {
      super(query);
    }

    protected Selector(Selector selector, IAggregate aggregate) {
      super(selector, aggregate);
    }

    @Override
    protected Selector aggregateSegment(IAggregate aggregate) {
      return new Selector(this, aggregate);
    }

    public Selector id(String _alias_) {
      return this.process(id, _alias_);
    }

    public Selector pv(String _alias_) {
      return this.process(pv, _alias_);
    }

    public Selector today(String _alias_) {
      return this.process(today, _alias_);
    }

    public Selector uv(String _alias_) {
      return this.process(uv, _alias_);
    }
  }

  /**
   * query/update where条件设置
   */
  abstract class EntityWhere<W extends WhereBase<W, U, ApiAuthStatisticsQuery>, U extends IWrapper<?, U, ApiAuthStatisticsQuery>> extends WhereBase<W, U, ApiAuthStatisticsQuery> {
    public EntityWhere(U wrapper) {
      super(wrapper);
    }

    protected EntityWhere(U wrapper, W where) {
      super(wrapper, where);
    }

    public NumericWhere<W, ApiAuthStatisticsQuery> id() {
      return this.set(id);
    }

    public NumericWhere<W, ApiAuthStatisticsQuery> pv() {
      return this.set(pv);
    }

    public NumericWhere<W, ApiAuthStatisticsQuery> today() {
      return this.set(today);
    }

    public NumericWhere<W, ApiAuthStatisticsQuery> uv() {
      return this.set(uv);
    }
  }

  /**
   * query where条件设置
   */
  class QueryWhere extends EntityWhere<QueryWhere, ApiAuthStatisticsQuery> {
    public QueryWhere(ApiAuthStatisticsQuery query) {
      super(query);
    }

    private QueryWhere(ApiAuthStatisticsQuery query, QueryWhere where) {
      super(query, where);
    }

    @Override
    protected QueryWhere buildOr(QueryWhere and) {
      return new QueryWhere((ApiAuthStatisticsQuery) this.wrapper, and);
    }
  }

  /**
   * update where条件设置
   */
  class UpdateWhere extends EntityWhere<UpdateWhere, ApiAuthStatisticsUpdate> {
    public UpdateWhere(ApiAuthStatisticsUpdate updater) {
      super(updater);
    }

    private UpdateWhere(ApiAuthStatisticsUpdate updater, UpdateWhere where) {
      super(updater, where);
    }

    @Override
    protected UpdateWhere buildOr(UpdateWhere and) {
      return new UpdateWhere((ApiAuthStatisticsUpdate) this.wrapper, and);
    }
  }

  /**
   * 分组设置
   */
  final class GroupBy extends GroupByBase<GroupBy, ApiAuthStatisticsQuery> implements ASegment<GroupBy> {
    public GroupBy(ApiAuthStatisticsQuery query) {
      super(query);
    }
  }

  /**
   * 分组Having条件设置
   */
  final class Having extends HavingBase<Having, ApiAuthStatisticsQuery> implements ASegment<HavingOperator<Having>> {
    public Having(ApiAuthStatisticsQuery query) {
      super(query);
    }

    protected Having(Having having, IAggregate aggregate) {
      super(having, aggregate);
    }

    @Override
    protected Having aggregateSegment(IAggregate aggregate) {
      return new Having(this, aggregate);
    }
  }

  /**
   * Query OrderBy设置
   */
  final class QueryOrderBy extends OrderByBase<QueryOrderBy, ApiAuthStatisticsQuery> implements ASegment<OrderByApply<QueryOrderBy, ApiAuthStatisticsQuery>> {
    public QueryOrderBy(ApiAuthStatisticsQuery query) {
      super(query);
    }
  }

  /**
   * Update OrderBy设置
   */
  final class UpdateOrderBy extends OrderByBase<UpdateOrderBy, ApiAuthStatisticsUpdate> implements ASegment<OrderByApply<UpdateOrderBy, ApiAuthStatisticsUpdate>> {
    public UpdateOrderBy(ApiAuthStatisticsUpdate updater) {
      super(updater);
    }
  }

  /**
   * Update set 设置
   */
  final class UpdateSetter extends UpdateBase<UpdateSetter, ApiAuthStatisticsUpdate> implements ASegment<UpdateApply<UpdateSetter, ApiAuthStatisticsUpdate>> {
    public UpdateSetter(ApiAuthStatisticsUpdate updater) {
      super(updater);
    }
  }
}
