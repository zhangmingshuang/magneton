package org.magneton.framework.core.design.processor;

import com.google.common.eventbus.EventBus;

import javax.annotation.Nullable;

/**
 * 事件总线执行器
 *
 * @author zhangmsh
 * @since 2025.2
 */
public class EventBusExecutor {

	private static final ThreadLocal<Object> HOLDER = new ThreadLocal<>();

	private static final EventBus EVENT_BUS = EventBusRegister.EVENT_BUS;

	@Nullable
	public static <T> T hold() {
		return (T) HOLDER.get();
	}

	public static Holder hold(Object object) {
		HOLDER.set(object);
		return Holder.INSTANCE;
	}

	public static void post(Object event) {
		EVENT_BUS.post(event);
	}

	public static class Holder {

		private static final Holder INSTANCE = new Holder();

		public void post(Object event) {
			try {
				EVENT_BUS.post(event);
			}
			finally {
				HOLDER.remove();
			}
		}

	}

}
