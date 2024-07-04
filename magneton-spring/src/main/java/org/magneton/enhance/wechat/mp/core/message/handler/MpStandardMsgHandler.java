package org.magneton.enhance.wechat.mp.core.message.handler;

import com.google.common.collect.Lists;
import org.magneton.enhance.wechat.mp.core.message.mode.Mode;
import org.magneton.enhance.wechat.mp.core.message.mode.StandardMsgMode;
import org.magneton.enhance.wechat.mp.core.message.pojo.MpInTextMsg;
import org.magneton.enhance.wechat.mp.core.message.pojo.MpOutMsg;

import javax.annotation.Nullable;
import java.util.List;

/**
 * 微信公众号标准消息处理器.
 *
 * @author zhangmsh.
 * @since 2024
 */
public interface MpStandardMsgHandler extends MpHandler {

	/**
	 * 支持的消息模式.
	 * @return 消息模式
	 */
	@Nullable
	@Override
	default List<? extends Mode> supportMode() {
		return Lists.newArrayList(StandardMsgMode.values());
	}

	/**
	 * 处理消息
	 * @param textMsg 消息
	 * @return 处理结果
	 */
	MpOutMsg onTextMsg(MpInTextMsg textMsg);

}