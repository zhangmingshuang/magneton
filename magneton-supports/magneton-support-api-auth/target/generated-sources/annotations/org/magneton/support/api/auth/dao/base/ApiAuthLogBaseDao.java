package org.magneton.support.api.auth.dao.base;

import static org.magneton.support.api.auth.helper.ApiAuthLogMapping.MAPPING;

import cn.org.atool.fluent.mybatis.base.crud.IDefaultGetter;
import cn.org.atool.fluent.mybatis.base.dao.BaseDao;
import cn.org.atool.fluent.mybatis.base.mapper.IMapper;
import javax.annotation.Resource;
import org.magneton.support.api.auth.entity.ApiAuthLogDO;
import org.magneton.support.api.auth.mapper.ApiAuthLogMapper;
import org.magneton.support.api.auth.wrapper.ApiAuthLogQuery;
import org.magneton.support.api.auth.wrapper.ApiAuthLogUpdate;

/**
 *
 * ApiAuthLogBaseDao
 *
 * @author powered by FluentMybatis
 */
public abstract class ApiAuthLogBaseDao extends BaseDao<ApiAuthLogDO, ApiAuthLogQuery, ApiAuthLogUpdate> {
  protected ApiAuthLogMapper mapper;

  @Override
  public ApiAuthLogMapper mapper() {
    return mapper;
  }

  @Override
  protected IDefaultGetter defaults() {
    return MAPPING;
  }

  @Override
  @Resource(
      name = "fmApiAuthLogMapper"
  )
  public void setMapper(IMapper<ApiAuthLogDO> mapper) {
    this.mapper = (ApiAuthLogMapper)mapper;
  }
}
