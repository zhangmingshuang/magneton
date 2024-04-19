package org.magneton.module.wechat.mp.core.handler;

import org.magneton.module.wechat.mp.core.mode.Mode;

import javax.annotation.Nullable;
import java.util.List;

/**
 * 消息处理器.
 *
 * @author zhangmsh.
 * @since 2024
 */
public interface MpHandler {

	/**
	 * 支持处理的消息模式.
	 * @return 消息模式，如果返回null或者空集合则表示不支持任意的消息模式
	 */
	@Nullable
	List<? extends Mode> supportMode();

}