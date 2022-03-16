package org.magneton.support.admin.basic.mapper;

import static org.magneton.support.admin.basic.helper.AdminSysConfigMapping.MAPPING;

import cn.org.atool.fluent.mybatis.base.entity.IMapping;
import cn.org.atool.fluent.mybatis.base.mapper.IMapper;
import cn.org.atool.fluent.mybatis.base.mapper.IWrapperMapper;
import cn.org.atool.fluent.mybatis.mapper.PrinterMapper;
import java.util.List;
import java.util.function.Consumer;
import org.apache.ibatis.annotations.Mapper;
import org.magneton.support.admin.basic.entity.AdminSysConfigDO;
import org.magneton.support.admin.basic.wrapper.AdminSysConfigQuery;
import org.magneton.support.admin.basic.wrapper.AdminSysConfigUpdate;
import org.springframework.stereotype.Component;

/**
 *
 * AdminSysConfigMapper: Mapper接口
 *
 * @author powered by FluentMybatis
 */
@Mapper
@Component("fmAdminSysConfigMapper")
public interface AdminSysConfigMapper extends IWrapperMapper<AdminSysConfigDO, AdminSysConfigQuery, AdminSysConfigUpdate>, IMapper<AdminSysConfigDO> {
  @Override
  default IMapping mapping() {
    return MAPPING;
  }

  static List<String> print(int mode, Consumer<IWrapperMapper> simulators) {
    return PrinterMapper.print(mode, MAPPING, simulators);
  }
}
