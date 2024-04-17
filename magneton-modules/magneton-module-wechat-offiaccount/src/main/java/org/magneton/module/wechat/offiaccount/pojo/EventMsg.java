package org.magneton.module.wechat.offiaccount.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 事件消息.
 *
 * @author zhangmsh.
 * @since 1.0.0
 */
@Setter
@Getter
@ToString(callSuper = true)
public class EventMsg extends BaseMsg {

	/**
	 * 订阅
	 * <p>
	 * 如果是订阅事件，那么为true，如果是取消订阅事件，那么为false
	 */
	private boolean subscribe;

}