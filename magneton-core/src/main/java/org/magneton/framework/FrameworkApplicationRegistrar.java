package org.magneton.framework;

import cn.hutool.core.annotation.AnnotationUtil;
import org.magneton.foundation.exception.DuplicateFoundException;
import org.magneton.foundation.spi.SPILoader;
import org.magneton.framework.util.OrderUtil;

import javax.annotation.Priority;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 框架应用注册表
 *
 * @author zhangmsh.
 * @since 2024
 */
public class FrameworkApplicationRegistrar {

	/**
	 * 已注册的框架应用
	 */
	private static final List<FrameworkApplication> FRAMEWORK_APPLICATIONS = new CopyOnWriteArrayList<>();

	/**
	 * 优先的框架应用
	 */
	private static final List<FrameworkApplication> PRIORITY_FRAMEWORK_APPLICATIONS = new CopyOnWriteArrayList<>();

	static {
		// 加载通过ServiceApi注册的框架应用实现
		List<FrameworkApplication> spiFrameworkApplications = SPILoader.loadInstance(FrameworkApplication.class);
		for (FrameworkApplication spiFrameworkApplication : spiFrameworkApplications) {
			Priority priority = AnnotationUtil.getAnnotation(spiFrameworkApplication.getClass(), Priority.class);
			if (priority != null) {
				PRIORITY_FRAMEWORK_APPLICATIONS.add(spiFrameworkApplication);
			}
			else {
				FRAMEWORK_APPLICATIONS.add(spiFrameworkApplication);
			}
		}
	}

	public static int register(FrameworkApplication frameworkApplication) {
		if (FRAMEWORK_APPLICATIONS.contains(frameworkApplication)) {
			throw new DuplicateFoundException(frameworkApplication, "Duplicate framework application found.");
		}
		FRAMEWORK_APPLICATIONS.add(frameworkApplication);
		// 排序
		OrderUtil.sort(FRAMEWORK_APPLICATIONS);
		return FRAMEWORK_APPLICATIONS.size();
	}

	public static boolean unregister(FrameworkApplication frameworkApplication) {
		return FRAMEWORK_APPLICATIONS.remove(frameworkApplication);
	}

	public static List<FrameworkApplication> getFrameworkApplications() {
		return FRAMEWORK_APPLICATIONS;
	}

	public static List<FrameworkApplication> getPriorityFrameworkApplications() {
		return PRIORITY_FRAMEWORK_APPLICATIONS;
	}

}