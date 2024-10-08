package org.magneton.spring.test;

import com.google.auto.service.AutoService;
import org.magneton.spring.automator.lifecycle.FrameworkLifecycle;

import javax.annotation.Priority;
import java.util.concurrent.atomic.AtomicReference;

@Priority(2)
@AutoService(FrameworkLifecycle.class)
public class PriorityBLifeCycle implements FrameworkLifecycle {

	public static AtomicReference<Boolean> ref = new AtomicReference<>(false);

	@Override
	public void starting() {
		System.out.println("starting at PriorityBLifeCycle");
		ref.set(true);
	}

}