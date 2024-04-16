package org.magneton.framework;

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
public class MagnetonApplication {

	private static final Map<String, Object> CONTENT = new ConcurrentHashMap<>();

	public static void start() {
		List<FrameworkApplication> priorityFrameworkApplications = FrameworkApplicationRegistry
				.getPriorityFrameworkApplications();
		doStart(priorityFrameworkApplications);

		List<FrameworkApplication> frameworkApplications = FrameworkApplicationRegistry.getFrameworkApplications();
		doStart(frameworkApplications);
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

	private static void doStart(List<FrameworkApplication> frameworkApplications) {
		for (FrameworkApplication frameworkApplication : frameworkApplications) {
			frameworkApplication.starting();
		}
	}

}