package org.magneton.support.api.auth;

import org.magneton.support.api.auth.helper.ApiAuthLogMapping;
import org.magneton.support.api.auth.helper.ApiAuthStatisticsMapping;
import org.magneton.support.api.auth.helper.ApiAuthUserMapping;

/**
 *
 * Ref: 
 *  o - 查询器，更新器工厂类单例引用
 *  o - 应用所有Mapper Bean引用
 *  o - Entity关联对象延迟加载查询实现
 *
 * @author powered by FluentMybatis
 */
public interface Ref {
  /**
   * 所有Entity FieldMapping引用
   */
  interface Field {
    final class ApiAuthLog extends ApiAuthLogMapping {
    }

    final class ApiAuthStatistics extends ApiAuthStatisticsMapping {
    }

    final class ApiAuthUser extends ApiAuthUserMapping {
    }
  }

  interface Query {
    ApiAuthLogMapping apiAuthLog = ApiAuthLogMapping.MAPPING;

    ApiAuthStatisticsMapping apiAuthStatistics = ApiAuthStatisticsMapping.MAPPING;

    ApiAuthUserMapping apiAuthUser = ApiAuthUserMapping.MAPPING;
  }
}
