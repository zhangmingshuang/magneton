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

	public static void absentPut(String key, Object value) {
		Object exist = CONTENT.put(key, value);
		if (exist != null) {
			throw new IllegalArgumentException("key: " + key + " already exist.");
		}
	}

	public static Object put(String key, Object value) {
		return CONTENT.put(key, value);
	}

	public static Object putIfAbsent(String key, Object value) {
		return CONTENT.putIfAbsent(key, value);
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