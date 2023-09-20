package org.magneton.foundation.exception;

import com.google.common.base.Strings;

/**
 * 重复异常.
 *
 * @author zhangmsh
 * @since 1.0.0
 */
public class DuplicateFoundException extends RuntimeException {

	private static final long serialVersionUID = 3361116627142896802L;

	private final Object pre;

	private final Object next;

	public DuplicateFoundException(Object pre, Object next) {
		super(Strings.lenientFormat("duplicate %s and %s founded.", pre instanceof Class ? pre : pre.getClass(),
				next instanceof Class ? next : next.getClass()));
		this.pre = pre;
		this.next = next;
	}

}
