package org.magneton.enhance.wechat.mp.core.router;

import org.magneton.spring.util.OrderUtil;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 消息分发处理器注册中心.
 *
 * @author zhangmsh.
 * @since 2024
 */
public class MpDispatchProcessorRegistrar {

	private static final List<MpDispatchProcessor> PROCESSORS = new CopyOnWriteArrayList<>();

	/**
	 * 注册处理器.
	 * @param processor 处理器
	 */
	public static void register(MpDispatchProcessor processor) {
		PROCESSORS.add(processor);
		OrderUtil.sort(PROCESSORS);
	}

	/**
	 * 获取所有处理器.
	 * @return 处理器
	 */
	public static List<MpDispatchProcessor> list() {
		return PROCESSORS;
	}

	/**
	 * 移除处理器.
	 * @param processor 处理器
	 */
	public static void remove(MpDispatchProcessor processor) {
		PROCESSORS.remove(processor);
	}

}