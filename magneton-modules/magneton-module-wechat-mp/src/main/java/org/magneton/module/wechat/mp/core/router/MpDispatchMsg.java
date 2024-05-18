package org.magneton.module.wechat.mp.core.router;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.magneton.module.wechat.mp.core.message.mode.Mode;
import org.magneton.module.wechat.mp.core.message.pojo.InBaseMsg;

import java.util.Map;

/**
 * 当前的分发消息.
 *
 * @author zhangmsh.
 * @since 2024
 */
@Setter
@Getter
@ToString(callSuper = true)
public class MpDispatchMsg extends InBaseMsg {

	private Mode mode;

	private Map<String, Object> body;

}