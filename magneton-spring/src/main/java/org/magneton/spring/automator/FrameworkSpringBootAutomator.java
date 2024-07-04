package org.magneton.spring.automator;

import org.magneton.spring.automator.lifecycle.FrameworkLifecycleRunner;
import org.springframework.boot.ConfigurableBootstrapContext;
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

	@Override
	public void starting(ConfigurableBootstrapContext bootstrapContext) {
		FrameworkLifecycleRunner.put("magneton.spring.bootstrap.context", bootstrapContext);
		FrameworkLifecycleRunner.starting();
	}

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE + 5;
	}

}