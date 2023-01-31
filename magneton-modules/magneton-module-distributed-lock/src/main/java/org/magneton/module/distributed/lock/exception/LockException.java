package org.magneton.module.distributed.lock.exception;

/**
 * 锁异常.
 *
 * @author zhangmsh 2022/2/11
 * @since 1.2.0
 */
public class LockException extends RuntimeException {

	public LockException(String message, Throwable cause) {
		super(message, cause);
	}

}
