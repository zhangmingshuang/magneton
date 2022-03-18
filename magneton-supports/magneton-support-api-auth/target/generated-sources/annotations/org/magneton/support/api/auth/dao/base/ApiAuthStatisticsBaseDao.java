package org.magneton.support.api.auth.dao.base;

import static org.magneton.support.api.auth.helper.ApiAuthStatisticsMapping.MAPPING;

import cn.org.atool.fluent.mybatis.base.crud.IDefaultGetter;
import cn.org.atool.fluent.mybatis.base.dao.BaseDao;
import cn.org.atool.fluent.mybatis.base.mapper.IMapper;
import javax.annotation.Resource;
import org.magneton.support.api.auth.entity.ApiAuthStatisticsDO;
import org.magneton.support.api.auth.mapper.ApiAuthStatisticsMapper;
import org.magneton.support.api.auth.wrapper.ApiAuthStatisticsQuery;
import org.magneton.support.api.auth.wrapper.ApiAuthStatisticsUpdate;

/**
 *
 * ApiAuthStatisticsBaseDao
 *
 * @author powered by FluentMybatis
 */
public abstract class ApiAuthStatisticsBaseDao extends BaseDao<ApiAuthStatisticsDO, ApiAuthStatisticsQuery, ApiAuthStatisticsUpdate> {
  protected ApiAuthStatisticsMapper mapper;

  @Override
  public ApiAuthStatisticsMapper mapper() {
    return mapper;
  }

  @Override
  protected IDefaultGetter defaults() {
    return MAPPING;
  }

  @Override
  @Resource(
      name = "fmApiAuthStatisticsMapper"
  )
  public void setMapper(IMapper<ApiAuthStatisticsDO> mapper) {
    this.mapper = (ApiAuthStatisticsMapper)mapper;
  }
}
