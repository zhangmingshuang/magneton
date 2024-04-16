package org.magneton.module.wechat.offiaccount.handler;

import org.magneton.module.wechat.offiaccount.pojo.InputTextMsg;
import org.magneton.module.wechat.offiaccount.pojo.OutputTextMsg;

/**
 * 消息处理器.
 *
 * @author zhangmsh.
 * @since 2024
 */
public interface MessageHandler {

	/**
	 * 处理消息
	 * @param textMsg 消息
	 * @return 处理结果
	 */
	OutputTextMsg onTextMsg(InputTextMsg textMsg);

}