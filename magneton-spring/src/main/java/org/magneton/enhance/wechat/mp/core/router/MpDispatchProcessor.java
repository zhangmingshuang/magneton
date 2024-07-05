package org.magneton.enhance.wechat.mp.core.router;

import org.magneton.enhance.wechat.mp.core.message.pojo.MpOutTextMsg;
import org.springframework.core.Ordered;

import javax.annotation.Nullable;

/**
 * 消息分发处理器.
 *
 * @author zhangmsh.
 * @since 2024
 */
public interface MpDispatchProcessor {

	/**
	 * 获取处理器的优先级
	 * @return 优先级
	 */
	default int getOrder() {
		return Ordered.LOWEST_PRECEDENCE - 1000;
	}

	/**
	 * 消息分发前置处理器
	 * @param appid appid，用来区分不同的公众号
	 * @param dispatchMsg 当前的分发消息
	 * @return 如果返回null则表示可以继续分发，否则返回的消息将被直接返回给用户
	 */
	@Nullable
	MpOutTextMsg preDispatch(String appid, MpDispatchMsg dispatchMsg);

}