package org.magneton.support.basic.dao.base;

import static org.magneton.support.basic.helper.BasicSysConfigMapping.MAPPING;

import cn.org.atool.fluent.mybatis.base.crud.IDefaultGetter;
import cn.org.atool.fluent.mybatis.base.dao.BaseDao;
import cn.org.atool.fluent.mybatis.base.mapper.IMapper;
import javax.annotation.Resource;
import org.magneton.support.basic.entity.BasicSysConfigDO;
import org.magneton.support.basic.mapper.BasicSysConfigMapper;
import org.magneton.support.basic.wrapper.BasicSysConfigQuery;
import org.magneton.support.basic.wrapper.BasicSysConfigUpdate;

/**
 *
 * BasicSysConfigBaseDao
 *
 * @author powered by FluentMybatis
 */
public abstract class BasicSysConfigBaseDao extends BaseDao<BasicSysConfigDO, BasicSysConfigQuery, BasicSysConfigUpdate> {
  protected BasicSysConfigMapper mapper;

  @Override
  public BasicSysConfigMapper mapper() {
    return mapper;
  }

  @Override
  protected IDefaultGetter defaults() {
    return MAPPING;
  }

  @Override
  @Resource(
      name = "fmBasicSysConfigMapper"
  )
  public void setMapper(IMapper<BasicSysConfigDO> mapper) {
    this.mapper = (BasicSysConfigMapper)mapper;
  }
}
