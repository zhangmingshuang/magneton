package org.magneton.module.safedog.geetest;

import org.magneton.core.base.Preconditions;
import org.magneton.module.safedog.TodoSafeDog;

/**
 * 极验行为安全狗.
 *
 * @author zhangmsh 09/04/2022
 * @since 2.0.8
 */
public class GeeTestTodoSafeDog implements TodoSafeDog<GeeTestBody> {

	private final GeeTestTodoConfig config;

	public GeeTestTodoSafeDog(GeeTestTodoConfig config) {
		this.config = Preconditions.checkNotNull(config);
	}

	@Override
	public boolean validate(GeeTestBody body) {
		return false;
	}

}
