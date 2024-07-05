package org.magneton.spring.automator.lifecycle;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 框架应用.
 *
 * @author zhangmsh.
 * @since 2024
 */
public class FrameworkLifecycleRunner {

	private static final Map<String, Object> CONTENT = new ConcurrentHashMap<>();

	public static void starting() {
		List<FrameworkLifecycle> priorityFrameworkLifecycles = FrameworkLifecycleRegister
			.getPriorityFrameworkApplications();
		doStarting(priorityFrameworkLifecycles);

		List<FrameworkLifecycle> frameworkLifecycles = FrameworkLifecycleRegister.getFrameworkApplications();
		doStarting(frameworkLifecycles);
	}

	/**
	 * 安全的添加.如果key已经存在则抛出异常.
	 * @param key key
	 * @param value value
	 */
	public static void safePut(String key, Object value) {
		Object exist = CONTENT.put(key, value);
		if (exist != null) {
			throw new IllegalArgumentException("key: " + key + " already exist.");
		}
	}

	/**
	 * 添加. 如果key已经存在则覆盖.
	 * @param key key
	 * @param value value
	 * @return 旧的值
	 */
	@Nullable
	public static Object put(String key, Object value) {
		return CONTENT.put(key, value);
	}

	/**
	 * 添加. 如果key已经存在则不添加.不存在则添加.
	 * @param key key
	 * @param value value
	 * @return 旧的值
	 */
	public static Object putIfAbsent(String key, Object value) {
		return CONTENT.putIfAbsent(key, value);
	}

	@Nullable
	public static Object remove(String key) {
		return CONTENT.remove(key);
	}

	@Nullable
	public static Object get(String key) {
		return CONTENT.get(key);
	}

	private static void doStarting(List<FrameworkLifecycle> frameworkLifecycles) {
		for (FrameworkLifecycle frameworkLifecycle : frameworkLifecycles) {
			frameworkLifecycle.starting();
		}
	}

}