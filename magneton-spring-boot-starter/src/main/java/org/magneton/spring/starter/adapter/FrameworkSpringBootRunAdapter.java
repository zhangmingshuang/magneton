package org.magneton.spring.starter.adapter;

import org.magneton.annotation.SpringFactories;
import org.magneton.framework.MagnetonApplication;
import org.springframework.boot.ConfigurableBootstrapContext;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.core.Ordered;

/**
 * Spring Boot Run Adapter.
 *
 * @author zhangmsh
 */
@SpringFactories
public class FrameworkSpringBootRunAdapter implements SpringApplicationRunListener, Ordered {

	@Override
	public void starting(ConfigurableBootstrapContext bootstrapContext) {
		MagnetonApplication.put("magneton.spring.bootstrap.context", bootstrapContext);
		MagnetonApplication.start();
	}

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE + 5;
	}

}