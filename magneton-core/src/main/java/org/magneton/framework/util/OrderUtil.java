package org.magneton.framework.util;

import org.magneton.framework.Ordered;

import java.util.List;

/**
 * 排序工具.
 *
 * @author zhangmsh.
 * @since 2024
 */
public class OrderUtil {

	private static int getOrder(Object obj) {
		if (obj instanceof Ordered) {
			return ((Ordered) obj).getOrder();
		}
		return Ordered.LOWEST_PRECEDENCE;
	}

	public static void sort(List<?> objects) {
		objects.sort((o1, o2) -> {
			int p1 = getOrder(o1);
			int p2 = getOrder(o2);
			return Integer.compare(p1, p2);
		});
	}

}