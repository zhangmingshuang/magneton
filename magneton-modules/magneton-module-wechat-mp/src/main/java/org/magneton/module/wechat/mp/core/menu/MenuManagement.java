package org.magneton.module.wechat.mp.core.menu;

import org.magneton.core.Result;

/**
 * 菜单管理.
 *
 * @author zhangmsh.
 * @since 2024
 */
public interface MenuManagement {

	/**
	 * 创建菜单.
	 * @apiNote 包括新增和修改，修改相当于覆盖之前的菜单。
	 * @param json 菜单的json字符串
	 * @return 创建结果
	 */
	Result<Void> create(String json);

}