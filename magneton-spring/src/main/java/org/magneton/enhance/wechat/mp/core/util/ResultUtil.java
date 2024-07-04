package org.magneton.enhance.wechat.mp.core.util;

/**
 * 返回结果工具.
 *
 * @author zhangmsh.
 * @since 2024
 */
public class ResultUtil {

	public static boolean ok(Integer code) {
		return code == null || code == 0;
	}

}