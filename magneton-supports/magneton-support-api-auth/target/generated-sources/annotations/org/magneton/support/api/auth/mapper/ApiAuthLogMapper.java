package org.magneton.support.api.auth.mapper;

import static org.magneton.support.api.auth.helper.ApiAuthLogMapping.MAPPING;

import cn.org.atool.fluent.mybatis.base.entity.IMapping;
import cn.org.atool.fluent.mybatis.base.mapper.IMapper;
import cn.org.atool.fluent.mybatis.base.mapper.IWrapperMapper;
import cn.org.atool.fluent.mybatis.mapper.PrinterMapper;
import java.util.List;
import java.util.function.Consumer;
import org.apache.ibatis.annotations.Mapper;
import org.magneton.support.api.auth.entity.ApiAuthLogDO;
import org.magneton.support.api.auth.wrapper.ApiAuthLogQuery;
import org.magneton.support.api.auth.wrapper.ApiAuthLogUpdate;
import org.springframework.stereotype.Component;

/**
 *
 * ApiAuthLogMapper: Mapper接口
 *
 * @author powered by FluentMybatis
 */
@Mapper
@Component("fmApiAuthLogMapper")
public interface ApiAuthLogMapper extends IWrapperMapper<ApiAuthLogDO, ApiAuthLogQuery, ApiAuthLogUpdate>, IMapper<ApiAuthLogDO> {
  @Override
  default IMapping mapping() {
    return MAPPING;
  }

  static List<String> print(int mode, Consumer<IWrapperMapper> simulators) {
    return PrinterMapper.print(mode, MAPPING, simulators);
  }
}
