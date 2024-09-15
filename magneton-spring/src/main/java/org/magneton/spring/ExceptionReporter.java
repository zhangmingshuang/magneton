package org.magneton.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * error print if running failed.
 *
 * @author zhangmsh.
 * @since 8.18.0
 */
public class ExceptionReporter implements SpringApplicationRunListener {

	public ExceptionReporter(SpringApplication application, String[] args) {
		// ignore. for init by spring boot. getListeners.
	}

	@Override
	public void failed(ConfigurableApplicationContext context, Throwable exception) {
		exception.printStackTrace();
	}

}
