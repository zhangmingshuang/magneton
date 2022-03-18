package org.magneton.support.api.auth.helper;

import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.assertNotNull;
import static org.magneton.support.api.auth.helper.ApiAuthUserMapping.*;

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
import org.magneton.support.api.auth.wrapper.ApiAuthUserQuery;
import org.magneton.support.api.auth.wrapper.ApiAuthUserUpdate;

/**
 *
 * ApiAuthUserSegment
 *
 * @author powered by FluentMybatis
 */
@SuppressWarnings({"unused", "rawtypes", "unchecked"})
public interface ApiAuthUserSegment {
  interface ASegment<R> {
    R set(FieldMapping fieldMapping);

    default R id() {
      return this.set(id);
    }

    default R account() {
      return this.set(account);
    }

    default R additional() {
      return this.set(additional);
    }

    default R createAdditional() {
      return this.set(createAdditional);
    }

    default R createTime() {
      return this.set(createTime);
    }

    default R pwd() {
      return this.set(pwd);
    }

    default R pwdSalt() {
      return this.set(pwdSalt);
    }

    default R removed() {
      return this.set(removed);
    }

    default R status() {
      return this.set(status);
    }
  }

  /**
   * select字段设置
   */
  final class Selector extends SelectorBase<Selector, ApiAuthUserQuery> implements ASegment<Selector> {
    public Selector(ApiAuthUserQuery query) {
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

    public Selector account(String _alias_) {
      return this.process(account, _alias_);
    }

    public Selector additional(String _alias_) {
      return this.process(additional, _alias_);
    }

    public Selector createAdditional(String _alias_) {
      return this.process(createAdditional, _alias_);
    }

    public Selector createTime(String _alias_) {
      return this.process(createTime, _alias_);
    }

    public Selector pwd(String _alias_) {
      return this.process(pwd, _alias_);
    }

    public Selector pwdSalt(String _alias_) {
      return this.process(pwdSalt, _alias_);
    }

    public Selector removed(String _alias_) {
      return this.process(removed, _alias_);
    }

    public Selector status(String _alias_) {
      return this.process(status, _alias_);
    }
  }

  /**
   * query/update where条件设置
   */
  abstract class EntityWhere<W extends WhereBase<W, U, ApiAuthUserQuery>, U extends IWrapper<?, U, ApiAuthUserQuery>> extends WhereBase<W, U, ApiAuthUserQuery> {
    public EntityWhere(U wrapper) {
      super(wrapper);
    }

    protected EntityWhere(U wrapper, W where) {
      super(wrapper, where);
    }

    public NumericWhere<W, ApiAuthUserQuery> id() {
      return this.set(id);
    }

    public StringWhere<W, ApiAuthUserQuery> account() {
      return this.set(account);
    }

    public StringWhere<W, ApiAuthUserQuery> additional() {
      return this.set(additional);
    }

    public StringWhere<W, ApiAuthUserQuery> createAdditional() {
      return this.set(createAdditional);
    }

    public NumericWhere<W, ApiAuthUserQuery> createTime() {
      return this.set(createTime);
    }

    public StringWhere<W, ApiAuthUserQuery> pwd() {
      return this.set(pwd);
    }

    public StringWhere<W, ApiAuthUserQuery> pwdSalt() {
      return this.set(pwdSalt);
    }

    public NumericWhere<W, ApiAuthUserQuery> removed() {
      return this.set(removed);
    }

    public NumericWhere<W, ApiAuthUserQuery> status() {
      return this.set(status);
    }
  }

  /**
   * query where条件设置
   */
  class QueryWhere extends EntityWhere<QueryWhere, ApiAuthUserQuery> {
    public QueryWhere(ApiAuthUserQuery query) {
      super(query);
    }

    private QueryWhere(ApiAuthUserQuery query, QueryWhere where) {
      super(query, where);
    }

    @Override
    protected QueryWhere buildOr(QueryWhere and) {
      return new QueryWhere((ApiAuthUserQuery) this.wrapper, and);
    }
  }

  /**
   * update where条件设置
   */
  class UpdateWhere extends EntityWhere<UpdateWhere, ApiAuthUserUpdate> {
    public UpdateWhere(ApiAuthUserUpdate updater) {
      super(updater);
    }

    private UpdateWhere(ApiAuthUserUpdate updater, UpdateWhere where) {
      super(updater, where);
    }

    @Override
    protected UpdateWhere buildOr(UpdateWhere and) {
      return new UpdateWhere((ApiAuthUserUpdate) this.wrapper, and);
    }
  }

  /**
   * 分组设置
   */
  final class GroupBy extends GroupByBase<GroupBy, ApiAuthUserQuery> implements ASegment<GroupBy> {
    public GroupBy(ApiAuthUserQuery query) {
      super(query);
    }
  }

  /**
   * 分组Having条件设置
   */
  final class Having extends HavingBase<Having, ApiAuthUserQuery> implements ASegment<HavingOperator<Having>> {
    public Having(ApiAuthUserQuery query) {
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
  final class QueryOrderBy extends OrderByBase<QueryOrderBy, ApiAuthUserQuery> implements ASegment<OrderByApply<QueryOrderBy, ApiAuthUserQuery>> {
    public QueryOrderBy(ApiAuthUserQuery query) {
      super(query);
    }
  }

  /**
   * Update OrderBy设置
   */
  final class UpdateOrderBy extends OrderByBase<UpdateOrderBy, ApiAuthUserUpdate> implements ASegment<OrderByApply<UpdateOrderBy, ApiAuthUserUpdate>> {
    public UpdateOrderBy(ApiAuthUserUpdate updater) {
      super(updater);
    }
  }

  /**
   * Update set 设置
   */
  final class UpdateSetter extends UpdateBase<UpdateSetter, ApiAuthUserUpdate> implements ASegment<UpdateApply<UpdateSetter, ApiAuthUserUpdate>> {
    public UpdateSetter(ApiAuthUserUpdate updater) {
      super(updater);
    }
  }
}
