package org.magneton.spy.core;

import org.magneton.core.base.Strings;

/**
 * @author zhangmsh 2021/7/28
 * @since 1.0.0
 */
public class DuplicateException extends RuntimeException {

	private static final long serialVersionUID = 6767205210590135690L;

	public DuplicateException(String name, Class a, Class b) {
		super(Strings.lenientFormat("the same protocol %s on %s and %s", name, a, b));
	}

}
