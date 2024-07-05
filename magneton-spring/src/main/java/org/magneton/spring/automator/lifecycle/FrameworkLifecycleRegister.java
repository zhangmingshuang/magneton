package org.magneton.spring.automator.lifecycle;

import cn.hutool.core.annotation.AnnotationUtil;
import org.magneton.spring.core.exception.DuplicateFoundException;
import org.magneton.spring.core.foundation.spi.SPILoader;
import org.magneton.spring.util.OrderUtil;

import javax.annotation.Priority;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 框架应用注册表
 *
 * @author zhangmsh.
 * @since 2024
 */
public class FrameworkLifecycleRegister {

	/**
	 * 已注册的框架应用
	 */
	private static final List<FrameworkLifecycle> FRAMEWORK_APPLICATIONS = new CopyOnWriteArrayList<>();

	/**
	 * 优先的框架应用
	 */
	private static final List<FrameworkLifecycle> PRIORITY_FRAMEWORK_APPLICATIONS = new CopyOnWriteArrayList<>();

	static {
		// 加载通过ServiceApi注册的框架应用实现
		List<FrameworkLifecycle> spiFrameworkLifecycles = SPILoader.loadInstance(FrameworkLifecycle.class);
		for (FrameworkLifecycle spiFrameworkLifecycle : spiFrameworkLifecycles) {
			Priority priority = AnnotationUtil.getAnnotation(spiFrameworkLifecycle.getClass(), Priority.class);
			if (priority != null) {
				PRIORITY_FRAMEWORK_APPLICATIONS.add(spiFrameworkLifecycle);
				OrderUtil.sort(PRIORITY_FRAMEWORK_APPLICATIONS);
			}
			else {
				FRAMEWORK_APPLICATIONS.add(spiFrameworkLifecycle);
				OrderUtil.sort(FRAMEWORK_APPLICATIONS);
			}
		}
	}

	public static int register(FrameworkLifecycle frameworkLifecycle) {
		if (FRAMEWORK_APPLICATIONS.contains(frameworkLifecycle)) {
			throw new DuplicateFoundException(frameworkLifecycle, "Duplicate framework application found.");
		}
		FRAMEWORK_APPLICATIONS.add(frameworkLifecycle);
		// 排序
		OrderUtil.sort(FRAMEWORK_APPLICATIONS);
		return FRAMEWORK_APPLICATIONS.size();
	}

	public static boolean unregister(FrameworkLifecycle frameworkLifecycle) {
		return FRAMEWORK_APPLICATIONS.remove(frameworkLifecycle);
	}

	public static List<FrameworkLifecycle> getFrameworkApplications() {
		return FRAMEWORK_APPLICATIONS;
	}

	public static List<FrameworkLifecycle> getPriorityFrameworkApplications() {
		return PRIORITY_FRAMEWORK_APPLICATIONS;
	}

}