package org.magneton.spring.test;

import com.google.auto.service.AutoService;
import org.magneton.spring.automator.lifecycle.FrameworkLifecycle;
import org.springframework.core.annotation.Order;

import java.util.concurrent.atomic.AtomicReference;

@Order(1)
@AutoService(FrameworkLifecycle.class)
public class BLifeCycle implements FrameworkLifecycle {

	public static AtomicReference<Boolean> ref = new AtomicReference<>(false);

	@Override
	public void starting() {
		System.out.println("starting at BLifeCycle");
		ref.set(true);
	}

}