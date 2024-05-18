package org.magneton.module.wechat.mp.core.message.handler;

import com.google.common.collect.Lists;
import org.magneton.module.wechat.mp.core.message.mode.EventPushMode;
import org.magneton.module.wechat.mp.core.message.mode.Mode;
import org.magneton.module.wechat.mp.core.message.pojo.MpEventMsg;

import javax.annotation.Nullable;
import java.util.List;

/**
 * 事件推送处理器.
 *
 * @author zhangmsh.
 * @since 2024
 */
public interface MpEventPushHandler extends MpHandler {

	/**
	 * 支持的消息模式.
	 * @return 消息模式
	 */
	@Nullable
	@Override
	default List<? extends Mode> supportMode() {
		return Lists.newArrayList(EventPushMode.values());
	}

	/**
	 * 订阅事件.
	 * @param baseMsg 消息
	 */
	void onSubscribeEvent(MpEventMsg baseMsg);

}