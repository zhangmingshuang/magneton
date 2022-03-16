package org.magneton.support.api.auth.mapper;

import static org.magneton.support.api.auth.helper.ApiAuthUserMapping.MAPPING;

import cn.org.atool.fluent.mybatis.base.entity.IMapping;
import cn.org.atool.fluent.mybatis.base.mapper.IMapper;
import cn.org.atool.fluent.mybatis.base.mapper.IWrapperMapper;
import cn.org.atool.fluent.mybatis.mapper.PrinterMapper;
import java.util.List;
import java.util.function.Consumer;
import org.apache.ibatis.annotations.Mapper;
import org.magneton.support.api.auth.entity.ApiAuthUserDO;
import org.magneton.support.api.auth.wrapper.ApiAuthUserQuery;
import org.magneton.support.api.auth.wrapper.ApiAuthUserUpdate;
import org.springframework.stereotype.Component;

/**
 *
 * ApiAuthUserMapper: Mapper接口
 *
 * @author powered by FluentMybatis
 */
@Mapper
@Component("fmApiAuthUserMapper")
public interface ApiAuthUserMapper extends IWrapperMapper<ApiAuthUserDO, ApiAuthUserQuery, ApiAuthUserUpdate>, IMapper<ApiAuthUserDO> {
  @Override
  default IMapping mapping() {
    return MAPPING;
  }

  static List<String> print(int mode, Consumer<IWrapperMapper> simulators) {
    return PrinterMapper.print(mode, MAPPING, simulators);
  }
}
