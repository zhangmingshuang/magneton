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
 * ApiAuthUserDO: 数据映射实体定义
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
@FluentMybatis(table = "api_auth_user", schema = "magneton", suffix = "DO")
public class ApiAuthUserDO extends RichEntity {

	private static final long serialVersionUID = 1L;

	@TableId("id")
	private Integer id;

	@TableField(value = "account", desc = "授权账号")
	private String account;

	@TableField(value = "additional", desc = "附加信息")
	private String additional;

	@TableField(value = "create_additional", desc = "创建附加信息")
	private String createAdditional;

	@TableField(value = "create_time", desc = "创建时间")
	private Long createTime;

	@TableField(value = "pwd", desc = "密码")
	private String pwd;

	@TableField(value = "pwd_salt", desc = "密码盐")
	private String pwdSalt;

	@TableField(value = "removed", desc = "是否已删除")
	private Integer removed;

	@TableField(value = "status", desc = "状态")
	private Integer status;

	@Override
	public final Class entityClass() {
		return ApiAuthUserDO.class;
	}

}
