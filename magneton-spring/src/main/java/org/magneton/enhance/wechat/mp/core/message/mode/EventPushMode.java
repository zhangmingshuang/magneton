package org.magneton.enhance.wechat.mp.core.message.mode;

import com.google.common.base.Strings;
import lombok.Getter;
import org.magneton.enhance.wechat.mp.core.message.handler.MpEventPushHandler;
import org.magneton.enhance.wechat.mp.core.message.handler.MpHandler;

import javax.annotation.Nullable;

/**
 * 事件消息.
 *
 * @author zhangmsh.
 * @since 2024
 */
@Getter
public enum EventPushMode implements Mode {

	/**
	 * 关注事件
	 */
	SUBSCRIBE("subscribe", null),

	/**
	 * 取消关注事件
	 */
	UNSUBSCRIBE("unsubscribe", null),

	/**
	 * 扫描带参数二维码事件. 用户未关注时，进行关注后的事件推送
	 */
	SCAN_UNSUBSCRIBE("subscribe", "#qrscene_"),
	/**
	 * 扫描带参数二维码事件. 用户已关注时的事件推送
	 */
	SCAN_SUBSCRIBE("scan", "*"),
	/**
	 * 上报地理位置
	 */
	LOCATION("location", null),

	/**
	 * 自定义菜单事件
	 */
	CLICK("click", "*"),

	/**
	 * 点击菜单跳转链接时的事件推送
	 */
	VIEW("view", "*"),;

	/**
	 * 事件的Event的类型
	 */
	private final String event;

	/**
	 * 事件的EventKey的条件，如果null表示没有限制，如果是*表示需要有值，但是可以是任意值，如果是#开头表示需要以XXX开头的值
	 */
	private final String conditionEventKey;

	EventPushMode(String event, String conditionEventKey) {
		this.event = event;
		this.conditionEventKey = conditionEventKey;
	}

	@Nullable
	public static EventPushMode of(@Nullable String event, @Nullable String eventKey) {
		for (EventPushMode eventPushMode : values()) {
			if (!eventPushMode.getEvent().equalsIgnoreCase(event)) {
				continue;
			}
			String conditionKey = eventPushMode.getConditionEventKey();
			if (!Strings.isNullOrEmpty(conditionKey) || !Strings.isNullOrEmpty(eventKey)) {
				if (!Strings.isNullOrEmpty(conditionKey) && !Strings.isNullOrEmpty(eventKey)) {
					continue;
				}
				if (Strings.isNullOrEmpty(conditionKey)) {
					continue;
				}
				if ("*".equals(conditionKey)) {
					return eventPushMode;
				}
				if (conditionKey.equalsIgnoreCase(eventKey)) {
					return eventPushMode;
				}
				if (conditionKey.charAt(0) == '#' && eventKey.startsWith(conditionKey.substring(1))) {
					return eventPushMode;
				}
				continue;
			}
			return eventPushMode;
		}
		return null;
	}

	public static final String MSG_TYPE = "event";

	@Override
	public ModeType modeType() {
		return ModeType.EVENT;
	}

	@Override
	public Class<? extends MpHandler> handlerType() {
		return MpEventPushHandler.class;
	}

}