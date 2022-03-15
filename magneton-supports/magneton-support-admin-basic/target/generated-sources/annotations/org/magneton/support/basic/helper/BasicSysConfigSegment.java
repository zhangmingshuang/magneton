package org.magneton.support.basic.helper;

import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.assertNotNull;
import static org.magneton.support.basic.helper.BasicSysConfigMapping.*;

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
import cn.org.atool.fluent.mybatis.segment.where.BooleanWhere;
import cn.org.atool.fluent.mybatis.segment.where.NumericWhere;
import cn.org.atool.fluent.mybatis.segment.where.ObjectWhere;
import cn.org.atool.fluent.mybatis.segment.where.StringWhere;
import org.magneton.support.basic.wrapper.BasicSysConfigQuery;
import org.magneton.support.basic.wrapper.BasicSysConfigUpdate;

/**
 *
 * BasicSysConfigSegment
 *
 * @author powered by FluentMybatis
 */
@SuppressWarnings({"unused", "rawtypes", "unchecked"})
public interface BasicSysConfigSegment {
  interface ASegment<R> {
    R set(FieldMapping fieldMapping);

    default R id() {
      return this.set(id);
    }

    default R builtIn() {
      return this.set(builtIn);
    }

    default R configKey() {
      return this.set(configKey);
    }

    default R configName() {
      return this.set(configName);
    }

    default R configValue() {
      return this.set(configValue);
    }

    default R createBy() {
      return this.set(createBy);
    }

    default R createTime() {
      return this.set(createTime);
    }

    default R remark() {
      return this.set(remark);
    }

    default R updateBy() {
      return this.set(updateBy);
    }

    default R updateTime() {
      return this.set(updateTime);
    }
  }

  /**
   * select字段设置
   */
  final class Selector extends SelectorBase<Selector, BasicSysConfigQuery> implements ASegment<Selector> {
    public Selector(BasicSysConfigQuery query) {
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

    public Selector builtIn(String _alias_) {
      return this.process(builtIn, _alias_);
    }

    public Selector configKey(String _alias_) {
      return this.process(configKey, _alias_);
    }

    public Selector configName(String _alias_) {
      return this.process(configName, _alias_);
    }

    public Selector configValue(String _alias_) {
      return this.process(configValue, _alias_);
    }

    public Selector createBy(String _alias_) {
      return this.process(createBy, _alias_);
    }

    public Selector createTime(String _alias_) {
      return this.process(createTime, _alias_);
    }

    public Selector remark(String _alias_) {
      return this.process(remark, _alias_);
    }

    public Selector updateBy(String _alias_) {
      return this.process(updateBy, _alias_);
    }

    public Selector updateTime(String _alias_) {
      return this.process(updateTime, _alias_);
    }
  }

  /**
   * query/update where条件设置
   */
  abstract class EntityWhere<W extends WhereBase<W, U, BasicSysConfigQuery>, U extends IWrapper<?, U, BasicSysConfigQuery>> extends WhereBase<W, U, BasicSysConfigQuery> {
    public EntityWhere(U wrapper) {
      super(wrapper);
    }

    protected EntityWhere(U wrapper, W where) {
      super(wrapper, where);
    }

    public NumericWhere<W, BasicSysConfigQuery> id() {
      return this.set(id);
    }

    public BooleanWhere<W, BasicSysConfigQuery> builtIn() {
      return this.set(builtIn);
    }

    public StringWhere<W, BasicSysConfigQuery> configKey() {
      return this.set(configKey);
    }

    public StringWhere<W, BasicSysConfigQuery> configName() {
      return this.set(configName);
    }

    public StringWhere<W, BasicSysConfigQuery> configValue() {
      return this.set(configValue);
    }

    public StringWhere<W, BasicSysConfigQuery> createBy() {
      return this.set(createBy);
    }

    public ObjectWhere<W, BasicSysConfigQuery> createTime() {
      return this.set(createTime);
    }

    public StringWhere<W, BasicSysConfigQuery> remark() {
      return this.set(remark);
    }

    public StringWhere<W, BasicSysConfigQuery> updateBy() {
      return this.set(updateBy);
    }

    public ObjectWhere<W, BasicSysConfigQuery> updateTime() {
      return this.set(updateTime);
    }
  }

  /**
   * query where条件设置
   */
  class QueryWhere extends EntityWhere<QueryWhere, BasicSysConfigQuery> {
    public QueryWhere(BasicSysConfigQuery query) {
      super(query);
    }

    private QueryWhere(BasicSysConfigQuery query, QueryWhere where) {
      super(query, where);
    }

    @Override
    protected QueryWhere buildOr(QueryWhere and) {
      return new QueryWhere((BasicSysConfigQuery) this.wrapper, and);
    }
  }

  /**
   * update where条件设置
   */
  class UpdateWhere extends EntityWhere<UpdateWhere, BasicSysConfigUpdate> {
    public UpdateWhere(BasicSysConfigUpdate updater) {
      super(updater);
    }

    private UpdateWhere(BasicSysConfigUpdate updater, UpdateWhere where) {
      super(updater, where);
    }

    @Override
    protected UpdateWhere buildOr(UpdateWhere and) {
      return new UpdateWhere((BasicSysConfigUpdate) this.wrapper, and);
    }
  }

  /**
   * 分组设置
   */
  final class GroupBy extends GroupByBase<GroupBy, BasicSysConfigQuery> implements ASegment<GroupBy> {
    public GroupBy(BasicSysConfigQuery query) {
      super(query);
    }
  }

  /**
   * 分组Having条件设置
   */
  final class Having extends HavingBase<Having, BasicSysConfigQuery> implements ASegment<HavingOperator<Having>> {
    public Having(BasicSysConfigQuery query) {
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
  final class QueryOrderBy extends OrderByBase<QueryOrderBy, BasicSysConfigQuery> implements ASegment<OrderByApply<QueryOrderBy, BasicSysConfigQuery>> {
    public QueryOrderBy(BasicSysConfigQuery query) {
      super(query);
    }
  }

  /**
   * Update OrderBy设置
   */
  final class UpdateOrderBy extends OrderByBase<UpdateOrderBy, BasicSysConfigUpdate> implements ASegment<OrderByApply<UpdateOrderBy, BasicSysConfigUpdate>> {
    public UpdateOrderBy(BasicSysConfigUpdate updater) {
      super(updater);
    }
  }

  /**
   * Update set 设置
   */
  final class UpdateSetter extends UpdateBase<UpdateSetter, BasicSysConfigUpdate> implements ASegment<UpdateApply<UpdateSetter, BasicSysConfigUpdate>> {
    public UpdateSetter(BasicSysConfigUpdate updater) {
      super(updater);
    }
  }
}
