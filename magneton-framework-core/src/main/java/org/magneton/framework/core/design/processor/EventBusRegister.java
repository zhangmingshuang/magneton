package org.magneton.framework.core.design.processor;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.magneton.framework.core.design.EventBusComponent;

import java.lang.reflect.Method;

/**
 * 后置处理器注册器
 *
 * @author zhangmsh
 * @since 1.0.0
 */
@Slf4j
public class EventBusRegister {

	private static final ThreadLocal<Throwable> EXP = new ThreadLocal<>();

	protected static final EventBus EVENT_BUS = new EventBus(new SubscriberExceptionHandler() {
		@Override
		public void handleException(Throwable exception, SubscriberExceptionContext context) {
			Method method = context.getSubscriberMethod();
			String errorInfo = "Exception thrown by subscriber method " + method.getName() + '('
					+ method.getParameterTypes()[0].getName() + ')' + " on subscriber " + context.getSubscriber()
					+ " when dispatching event: " + context.getEvent();
			log.error(errorInfo);
			EXP.set(exception);

		}
	}) {

		@Override
		public void post(Object event) {
			super.post(event);
			try {
				Throwable throwable = EXP.get();
				if (throwable != null) {
					throw new EventBusException(throwable);
				}
			}
			finally {
				EXP.remove();
			}
		}

	};

	public static void register(Object bean, EventBusComponent eventBusComponent) {
		if (bean == null) {
			return;
		}
		// 获取Bean的所有方法
		EVENT_BUS.register(bean);
	}

}
