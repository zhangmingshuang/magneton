package org.magneton.spring.starter.launcher;

import java.util.Map;

import org.magneton.core.collect.Maps;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * .
 *
 * @author zhangmsh 2022/2/7
 * @since 1.2.0
 */
public class RunLauncherRunListener extends AbstractRunLauncher implements SpringApplicationRunListener {

	public RunLauncherRunListener(SpringApplication springApplication, String[] args) {
		super();
	}

	@Override
	public void started(ConfigurableApplicationContext context) {
		Map<String, RunLauncher> runLaunchers = context.getBeansOfType(RunLauncher.class);
		if (Maps.isNullOrEmpty(runLaunchers)) {
			return;
		}
		this.initSubsequentRunLaunchers(runLaunchers.values());
	}

}
