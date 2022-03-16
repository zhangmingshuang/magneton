package org.magneton.support.api.auth.entity;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.annotation.TableField;
import cn.org.atool.fluent.mybatis.annotation.TableId;
import cn.org.atool.fluent.mybatis.base.RichEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * ApiAuthStatisticsDO: 数据映射实体定义
 *
 * @author Powered By Fluent Mybatis
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FluentMybatis(table = "api_auth_statistics", schema = "magneton", suffix = "DO")
public class ApiAuthStatisticsDO extends RichEntity {

	private static final long serialVersionUID = 1L;

	@TableId("id")
	private Integer id;

	@TableField(value = "pv", desc = "pv")
	private Integer pv;

	@TableField(value = "today", desc = "天，yyyyMMdd")
	private Integer today;

	@TableField(value = "uv", desc = "uv")
	private Integer uv;

	@Override
	public final Class entityClass() {
		return ApiAuthStatisticsDO.class;
	}

}
