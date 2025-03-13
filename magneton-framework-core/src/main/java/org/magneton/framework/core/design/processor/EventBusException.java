package org.magneton.framework.core.design.processor;

import lombok.Getter;

/**
 * 事件总线异常
 *
 * @author zhangmsh
 * @since 2025.2
 */
@Getter
public class EventBusException extends RuntimeException {

	public EventBusException(Throwable exception) {
		super(exception);
	}

}
