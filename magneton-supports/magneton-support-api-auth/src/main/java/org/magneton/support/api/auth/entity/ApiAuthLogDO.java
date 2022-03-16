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
 * ApiAuthLogDO: 数据映射实体定义
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
@FluentMybatis(table = "api_auth_log", schema = "magneton", suffix = "DO")
public class ApiAuthLogDO extends RichEntity {

	private static final long serialVersionUID = 1L;

	@TableId("id")
	private Integer id;

	@TableField(value = "create_additional", desc = "创建附加信息")
	private String createAdditional;

	@TableField(value = "create_time", desc = "创建时间")
	private Long createTime;

	@TableField(value = "user_id", desc = "授权用户")
	private Integer userId;

	@Override
	public final Class entityClass() {
		return ApiAuthLogDO.class;
	}

}
