package org.magneton.module.wechat.mp.core.handler;

import com.google.common.collect.Lists;
import org.magneton.module.wechat.mp.core.mode.Mode;
import org.magneton.module.wechat.mp.core.mode.StandardMsgMode;
import org.magneton.module.wechat.mp.core.pojo.MpInTextMsg;
import org.magneton.module.wechat.mp.core.pojo.MpOutTextMsg;

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
	MpOutTextMsg onTextMsg(MpInTextMsg textMsg);

}