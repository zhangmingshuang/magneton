package org.magneton.cache.util;

import java.util.List;

/**
 * .
 *
 * @author zhangmsh 2021/6/24
 * @since 1.0.0
 */
public class Datas {

	private Datas() {
		// private
	}

	public static boolean isEmpty(byte[] bytes) {
		return bytes == null || bytes.length < 1;
	}

	public static boolean isEmpty(List<byte[]> list) {
		return list == null || list.isEmpty();
	}

}
