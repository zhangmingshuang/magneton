package org.magneton.module.wechat.mp.core.router;

import com.google.common.base.Preconditions;
import com.google.common.base.Verify;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.magneton.module.wechat.mp.core.MpContext;
import org.magneton.module.wechat.mp.core.message.handler.MpHandler;
import org.magneton.module.wechat.mp.core.message.handler.MpHandlerRegistrar;
import org.magneton.module.wechat.mp.core.message.mode.Mode;
import org.magneton.module.wechat.mp.core.message.mode.ModeParsers;
import org.magneton.module.wechat.mp.core.message.pojo.MpOutTextMsg;

import javax.annotation.Nullable;
import java.util.List;

/**
 * 消息路由.
 *
 * @author zhangmsh.
 * @since 2024
 */
public class DefaultDispatchRouter implements DispatchRouter {

	@Override
	public WxMpXmlOutMessage dispatch(WxMpXmlMessage inMessage) {
		Preconditions.checkNotNull(inMessage, "inMessage must not be null.");

		Mode msgMode = ModeParsers.parse(inMessage);

		MpHandler msgHandler = MpHandlerRegistrar.getHandler(msgMode);
		Verify.verifyNotNull(msgHandler, "not found message handler.");

		DispatchProcessor dispatchProcessor = msgMode.modeType().getDispatchProcessor();

		List<MpDispatchProcessor> mpDispatchProcessors = MpDispatchProcessorRegistrar.list();
		if (mpDispatchProcessors != null) {
			for (MpDispatchProcessor mpDispatchProcessor : mpDispatchProcessors) {
				MpDispatchMsg mpDispatchMsg = new MpDispatchMsg();
				mpDispatchMsg.setMode(msgMode);
				mpDispatchMsg.setBody(inMessage.getAllFieldsMap());
				mpDispatchMsg.setFromUser(inMessage.getFromUser());
				mpDispatchMsg.setToUser(inMessage.getToUser());
				mpDispatchMsg.setCreateTime(inMessage.getCreateTime());
				MpOutTextMsg preOutMessage = mpDispatchProcessor.preDispatch(MpContext.currentAppid(), mpDispatchMsg);
				if (preOutMessage != null) {
					return WxMpXmlOutMessage.TEXT().content(preOutMessage.getContent()).fromUser(inMessage.getToUser())
							.toUser(inMessage.getFromUser()).build();
				}
			}
		}
		return dispatchProcessor.process(msgMode, msgHandler, inMessage);
	}

	@Nullable
	protected WxMpXmlOutMessage preDispatch(String appid, WxMpXmlMessage inMessage) {
		List<MpDispatchProcessor> processors = MpDispatchProcessorRegistrar.list();
		if (processors == null) {
			return null;
		}
		MpDispatchMsg dispatchMsg = new MpDispatchMsg();
		dispatchMsg.setBody(inMessage.getAllFieldsMap());
		dispatchMsg.setFromUser(inMessage.getFromUser());
		dispatchMsg.setToUser(inMessage.getToUser());
		dispatchMsg.setCreateTime(inMessage.getCreateTime());

		for (MpDispatchProcessor preProcessor : processors) {
			MpOutTextMsg outMessage = preProcessor.preDispatch(appid, dispatchMsg);
			if (outMessage != null) {
				return WxMpXmlOutMessage.TEXT().content(outMessage.getContent()).fromUser(inMessage.getToUser())
						.toUser(inMessage.getFromUser()).build();
			}
		}
		return null;
	}

}