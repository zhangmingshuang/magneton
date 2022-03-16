package org.magneton.support.api.auth.mapper;

import static org.magneton.support.api.auth.helper.ApiAuthStatisticsMapping.MAPPING;

import cn.org.atool.fluent.mybatis.base.entity.IMapping;
import cn.org.atool.fluent.mybatis.base.mapper.IMapper;
import cn.org.atool.fluent.mybatis.base.mapper.IWrapperMapper;
import cn.org.atool.fluent.mybatis.mapper.PrinterMapper;
import java.util.List;
import java.util.function.Consumer;
import org.apache.ibatis.annotations.Mapper;
import org.magneton.support.api.auth.entity.ApiAuthStatisticsDO;
import org.magneton.support.api.auth.wrapper.ApiAuthStatisticsQuery;
import org.magneton.support.api.auth.wrapper.ApiAuthStatisticsUpdate;
import org.springframework.stereotype.Component;

/**
 *
 * ApiAuthStatisticsMapper: Mapper接口
 *
 * @author powered by FluentMybatis
 */
@Mapper
@Component("fmApiAuthStatisticsMapper")
public interface ApiAuthStatisticsMapper extends IWrapperMapper<ApiAuthStatisticsDO, ApiAuthStatisticsQuery, ApiAuthStatisticsUpdate>, IMapper<ApiAuthStatisticsDO> {
  @Override
  default IMapping mapping() {
    return MAPPING;
  }

  static List<String> print(int mode, Consumer<IWrapperMapper> simulators) {
    return PrinterMapper.print(mode, MAPPING, simulators);
  }
}
