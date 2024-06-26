package org.magneton.module.wechat.mp.core.message.mode;

import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import org.magneton.foundation.spi.SPI;

/**
 * 模式解析器.
 *
 * @author zhangmsh.
 * @since 2024
 */
@SPI
public interface ModeParser {

	/**
	 * 解析消息.
	 * @param inMessage 输入消息
	 * @return 消息模式
	 */
	<T extends Mode> T parse(WxMpXmlMessage inMessage);

}