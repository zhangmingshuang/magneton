package org.magneton.spring.automator;

import org.magneton.spring.automator.lifecycle.FrameworkLifecycleRunner;
import org.springframework.boot.ConfigurableBootstrapContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.core.Ordered;

import javax.annotation.SpringFactories;

/**
 * Spring Boot Run Adapter.
 *
 * @author zhangmsh
 */
@SpringFactories
public class FrameworkSpringBootAutomator implements SpringApplicationRunListener, Ordered {

	public static final String FRAMEWORK_BOOTSTRAP_CONTEXT = "magneton.spring.bootstrap.context";

	private SpringApplication application;

	private String[] args;

	public FrameworkSpringBootAutomator(SpringApplication application, String[] args) {
		this.application = application;
		this.args = args;
	}

	@Override
	public void starting(ConfigurableBootstrapContext bootstrapContext) {
		FrameworkLifecycleRunner.put(FRAMEWORK_BOOTSTRAP_CONTEXT, bootstrapContext);
		FrameworkLifecycleRunner.starting();
	}

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE + 5;
	}

}