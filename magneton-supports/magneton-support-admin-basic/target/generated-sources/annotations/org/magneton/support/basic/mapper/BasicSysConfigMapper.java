package org.magneton.support.basic.mapper;

import static org.magneton.support.basic.helper.BasicSysConfigMapping.MAPPING;

import cn.org.atool.fluent.mybatis.base.entity.IMapping;
import cn.org.atool.fluent.mybatis.base.mapper.IMapper;
import cn.org.atool.fluent.mybatis.base.mapper.IWrapperMapper;
import cn.org.atool.fluent.mybatis.mapper.PrinterMapper;
import java.util.List;
import java.util.function.Consumer;
import org.apache.ibatis.annotations.Mapper;
import org.magneton.support.basic.entity.BasicSysConfigDO;
import org.magneton.support.basic.wrapper.BasicSysConfigQuery;
import org.magneton.support.basic.wrapper.BasicSysConfigUpdate;
import org.springframework.stereotype.Component;

/**
 *
 * BasicSysConfigMapper: Mapper接口
 *
 * @author powered by FluentMybatis
 */
@Mapper
@Component("fmBasicSysConfigMapper")
public interface BasicSysConfigMapper extends IWrapperMapper<BasicSysConfigDO, BasicSysConfigQuery, BasicSysConfigUpdate>, IMapper<BasicSysConfigDO> {
  @Override
  default IMapping mapping() {
    return MAPPING;
  }

  static List<String> print(int mode, Consumer<IWrapperMapper> simulators) {
    return PrinterMapper.print(mode, MAPPING, simulators);
  }
}
