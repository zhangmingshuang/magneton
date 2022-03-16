package org.magneton.support.api.auth.helper;

import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.assertNotNull;
import static org.magneton.support.api.auth.helper.ApiAuthLogMapping.*;

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
import cn.org.atool.fluent.mybatis.segment.where.StringWhere;
import org.magneton.support.api.auth.wrapper.ApiAuthLogQuery;
import org.magneton.support.api.auth.wrapper.ApiAuthLogUpdate;

/**
 *
 * ApiAuthLogSegment
 *
 * @author powered by FluentMybatis
 */
@SuppressWarnings({"unused", "rawtypes", "unchecked"})
public interface ApiAuthLogSegment {
  interface ASegment<R> {
    R set(FieldMapping fieldMapping);

    default R id() {
      return this.set(id);
    }

    default R createAdditional() {
      return this.set(createAdditional);
    }

    default R createTime() {
      return this.set(createTime);
    }

    default R userId() {
      return this.set(userId);
    }
  }

  /**
   * select字段设置
   */
  final class Selector extends SelectorBase<Selector, ApiAuthLogQuery> implements ASegment<Selector> {
    public Selector(ApiAuthLogQuery query) {
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

    public Selector createAdditional(String _alias_) {
      return this.process(createAdditional, _alias_);
    }

    public Selector createTime(String _alias_) {
      return this.process(createTime, _alias_);
    }

    public Selector userId(String _alias_) {
      return this.process(userId, _alias_);
    }
  }

  /**
   * query/update where条件设置
   */
  abstract class EntityWhere<W extends WhereBase<W, U, ApiAuthLogQuery>, U extends IWrapper<?, U, ApiAuthLogQuery>> extends WhereBase<W, U, ApiAuthLogQuery> {
    public EntityWhere(U wrapper) {
      super(wrapper);
    }

    protected EntityWhere(U wrapper, W where) {
      super(wrapper, where);
    }

    public NumericWhere<W, ApiAuthLogQuery> id() {
      return this.set(id);
    }

    public StringWhere<W, ApiAuthLogQuery> createAdditional() {
      return this.set(createAdditional);
    }

    public NumericWhere<W, ApiAuthLogQuery> createTime() {
      return this.set(createTime);
    }

    public NumericWhere<W, ApiAuthLogQuery> userId() {
      return this.set(userId);
    }
  }

  /**
   * query where条件设置
   */
  class QueryWhere extends EntityWhere<QueryWhere, ApiAuthLogQuery> {
    public QueryWhere(ApiAuthLogQuery query) {
      super(query);
    }

    private QueryWhere(ApiAuthLogQuery query, QueryWhere where) {
      super(query, where);
    }

    @Override
    protected QueryWhere buildOr(QueryWhere and) {
      return new QueryWhere((ApiAuthLogQuery) this.wrapper, and);
    }
  }

  /**
   * update where条件设置
   */
  class UpdateWhere extends EntityWhere<UpdateWhere, ApiAuthLogUpdate> {
    public UpdateWhere(ApiAuthLogUpdate updater) {
      super(updater);
    }

    private UpdateWhere(ApiAuthLogUpdate updater, UpdateWhere where) {
      super(updater, where);
    }

    @Override
    protected UpdateWhere buildOr(UpdateWhere and) {
      return new UpdateWhere((ApiAuthLogUpdate) this.wrapper, and);
    }
  }

  /**
   * 分组设置
   */
  final class GroupBy extends GroupByBase<GroupBy, ApiAuthLogQuery> implements ASegment<GroupBy> {
    public GroupBy(ApiAuthLogQuery query) {
      super(query);
    }
  }

  /**
   * 分组Having条件设置
   */
  final class Having extends HavingBase<Having, ApiAuthLogQuery> implements ASegment<HavingOperator<Having>> {
    public Having(ApiAuthLogQuery query) {
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
  final class QueryOrderBy extends OrderByBase<QueryOrderBy, ApiAuthLogQuery> implements ASegment<OrderByApply<QueryOrderBy, ApiAuthLogQuery>> {
    public QueryOrderBy(ApiAuthLogQuery query) {
      super(query);
    }
  }

  /**
   * Update OrderBy设置
   */
  final class UpdateOrderBy extends OrderByBase<UpdateOrderBy, ApiAuthLogUpdate> implements ASegment<OrderByApply<UpdateOrderBy, ApiAuthLogUpdate>> {
    public UpdateOrderBy(ApiAuthLogUpdate updater) {
      super(updater);
    }
  }

  /**
   * Update set 设置
   */
  final class UpdateSetter extends UpdateBase<UpdateSetter, ApiAuthLogUpdate> implements ASegment<UpdateApply<UpdateSetter, ApiAuthLogUpdate>> {
    public UpdateSetter(ApiAuthLogUpdate updater) {
      super(updater);
    }
  }
}
