package org.magneton.support.admin.basic;

import org.magneton.support.admin.basic.helper.AdminSysConfigMapping;

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
    final class AdminSysConfig extends AdminSysConfigMapping {
    }
  }

  interface Query {
    AdminSysConfigMapping adminSysConfig = AdminSysConfigMapping.MAPPING;
  }
}
