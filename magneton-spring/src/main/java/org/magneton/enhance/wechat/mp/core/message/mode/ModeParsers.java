package org.magneton.enhance.wechat.mp.core.message.mode;

import com.google.common.collect.Lists;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import org.magneton.foundation.spi.SPILoader;
import org.magneton.framework.util.OrderUtil;

import java.util.List;

/**
 * 消息模式解析器.
 *
 * @author zhangmsh.
 * @since 2024
 */
public class ModeParsers {

	private static final List<ModeParser> MODE_PARSERS;

	static {
		List<ModeParser> modeParsers = SPILoader.loadInstance(ModeParser.class);
		MODE_PARSERS = Lists.newArrayList(modeParsers);
		OrderUtil.sort(MODE_PARSERS);
	}

	public static <T extends Mode> T parse(WxMpXmlMessage inMessage) {
		for (ModeParser modeParser : MODE_PARSERS) {
			T mpModeContext = modeParser.parse(inMessage);
			if (mpModeContext != null) {
				return mpModeContext;
			}
		}
		throw new IllegalArgumentException("Not found mode parser. inMessage: " + inMessage);
	}

}