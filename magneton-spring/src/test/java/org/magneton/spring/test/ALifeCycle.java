package org.magneton.spring.test;

import com.google.auto.service.AutoService;
import org.magneton.spring.automator.lifecycle.FrameworkLifecycle;

import java.util.concurrent.atomic.AtomicReference;

@AutoService(FrameworkLifecycle.class)
public class ALifeCycle implements FrameworkLifecycle {

	public static AtomicReference<Boolean> ref = new AtomicReference<>(false);

	@Override
	public void starting() {
		System.out.println("starting at ALifeCycle");
		ref.set(true);
	}

}