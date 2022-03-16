package org.magneton.support.api.auth.dao.base;

import static org.magneton.support.api.auth.helper.ApiAuthUserMapping.MAPPING;

import cn.org.atool.fluent.mybatis.base.crud.IDefaultGetter;
import cn.org.atool.fluent.mybatis.base.dao.BaseDao;
import cn.org.atool.fluent.mybatis.base.mapper.IMapper;
import javax.annotation.Resource;
import org.magneton.support.api.auth.entity.ApiAuthUserDO;
import org.magneton.support.api.auth.mapper.ApiAuthUserMapper;
import org.magneton.support.api.auth.wrapper.ApiAuthUserQuery;
import org.magneton.support.api.auth.wrapper.ApiAuthUserUpdate;

/**
 *
 * ApiAuthUserBaseDao
 *
 * @author powered by FluentMybatis
 */
public abstract class ApiAuthUserBaseDao extends BaseDao<ApiAuthUserDO, ApiAuthUserQuery, ApiAuthUserUpdate> {
  protected ApiAuthUserMapper mapper;

  @Override
  public ApiAuthUserMapper mapper() {
    return mapper;
  }

  @Override
  protected IDefaultGetter defaults() {
    return MAPPING;
  }

  @Override
  @Resource(
      name = "fmApiAuthUserMapper"
  )
  public void setMapper(IMapper<ApiAuthUserDO> mapper) {
    this.mapper = (ApiAuthUserMapper)mapper;
  }
}
