package org.magneton.spring.launcher;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 全局初始化器.
 *
 * @author zhangmsh 2022/2/7
 * @since 1.2.0
 */
public class RunLauncherContextInitializer extends AbstractRunLauncher implements ApplicationContextInitializer {

	public RunLauncherContextInitializer() {
		super();
	}

	@Override
	public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
		// ignore.
	}

}
