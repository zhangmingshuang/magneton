package org.magneton.enhance.safedog.sign;

import java.util.List;
import java.util.Set;

/**
 * Key 排序器.
 *
 * @author zhangmsh.
 * @since 2023.9
 */
public interface SignKeySorter {

	/**
	 * 排序.
	 * @param keys key集合
	 * @return 排序后的key集合
	 */
	List<String> sort(Set<String> keys);

}
