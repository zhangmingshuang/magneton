package org.magneton.module.wechat.offiaccount.router;

import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import org.magneton.module.wechat.offiaccount.handler.MessageHandler;
import org.magneton.module.wechat.offiaccount.handler.MsgType;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 消息处理器注册表.
 *
 * @author zhangmsh.
 * @since 2024
 */
public class MessageRouterRegistry {

	/**
	 * 消息处理器.
	 */
	private static final Map<String, Object> HANDLERS = new ConcurrentHashMap<>();

	/**
	 * 注册消息处理器.
	 * @param handler 消息处理器
	 * @return 如果存在相同类型的消息处理器则返回旧的消息处理器，否则返回 {@code null}
	 */
	@Nullable
	@CanIgnoreReturnValue
	public static MessageHandler registerMsgRoute(MessageHandler handler) {
		Preconditions.checkNotNull(handler, "handler must not be null");
		return (MessageHandler) HANDLERS.put(MsgType.class.getName(), handler);
	}

	@Nullable
	public static MessageHandler getMsgHandler() {
		return (MessageHandler) HANDLERS.get(MsgType.class.getName());
	}

}