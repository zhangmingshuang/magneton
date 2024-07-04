package org.magneton.enhance.safedog.sign;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Key 排序器.
 *
 * @author zhangmsh.
 * @since 2023.9
 */
@Slf4j
public class DefaultSignKeySorter implements SignKeySorter {

	@Override
	public List<String> sort(Set<String> keys) {
		if (keys == null || keys.isEmpty()) {
			return Collections.emptyList();
		}
		List<String> sorted = Lists.newArrayList(keys);
		Collections.sort(sorted);
		if (log.isDebugEnabled()) {
			int i = 0;
			for (String key : sorted) {
				log.debug("sorted key : {}.{}", ++i, key);
			}
		}
		return sorted;
	}

}