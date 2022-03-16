package org.magneton.support.admin.basic.dao.base;

import static org.magneton.support.admin.basic.helper.AdminSysConfigMapping.MAPPING;

import cn.org.atool.fluent.mybatis.base.crud.IDefaultGetter;
import cn.org.atool.fluent.mybatis.base.dao.BaseDao;
import cn.org.atool.fluent.mybatis.base.mapper.IMapper;
import javax.annotation.Resource;
import org.magneton.support.admin.basic.entity.AdminSysConfigDO;
import org.magneton.support.admin.basic.mapper.AdminSysConfigMapper;
import org.magneton.support.admin.basic.wrapper.AdminSysConfigQuery;
import org.magneton.support.admin.basic.wrapper.AdminSysConfigUpdate;

/**
 *
 * AdminSysConfigBaseDao
 *
 * @author powered by FluentMybatis
 */
public abstract class AdminSysConfigBaseDao extends BaseDao<AdminSysConfigDO, AdminSysConfigQuery, AdminSysConfigUpdate> {
  protected AdminSysConfigMapper mapper;

  @Override
  public AdminSysConfigMapper mapper() {
    return mapper;
  }

  @Override
  protected IDefaultGetter defaults() {
    return MAPPING;
  }

  @Override
  @Resource(
      name = "fmAdminSysConfigMapper"
  )
  public void setMapper(IMapper<AdminSysConfigDO> mapper) {
    this.mapper = (AdminSysConfigMapper)mapper;
  }
}
