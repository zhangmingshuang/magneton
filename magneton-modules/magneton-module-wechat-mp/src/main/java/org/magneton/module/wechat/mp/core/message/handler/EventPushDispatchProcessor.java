package org.magneton.module.wechat.mp.core.message.handler;

import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.magneton.module.wechat.mp.core.message.mode.Mode;
import org.magneton.module.wechat.mp.core.message.pojo.MpEventMsg;
import org.magneton.module.wechat.mp.core.message.pojo.MpOutMsg;
import org.magneton.module.wechat.mp.core.router.DispatchProcessor;

/**
 * 事件消息处理器.
 * <p>
 * 用户在关注与取消关注公众号时，微信会把这个事件推送到开发者填写的URL。方便开发者给用户下发欢迎消息或者做账号的解绑。
 * 为保护用户数据隐私，开发者收到用户取消关注事件时需要删除该用户的所有信息。
 * <p>
 * 微信服务器在五秒内收不到响应会断掉连接，并且重新发起请求，总共重试三次。
 * <p>
 * 关于重试的消息排重，推荐使用FromUserName + CreateTime 排重。
 * <p>
 * 假如服务器无法保证在五秒内处理并回复，可以直接回复空串，微信服务器不会对此作任何处理，并且不会发起重试。
 *
 * @author zhangmsh.
 * @since 2024
 */
public class EventPushDispatchProcessor implements DispatchProcessor {

	@Override
	public WxMpXmlOutMessage doProcess(String appid, Mode mode, MpHandler msgHandler, WxMpXmlMessage inMessage) {
		String fromUser = inMessage.getFromUser();
		String toUser = inMessage.getToUser();
		String event = inMessage.getEvent();
		Long createTime = inMessage.getCreateTime();

		MpEventMsg mpEventMsg = new MpEventMsg();
		mpEventMsg.setAppId(appid);
		mpEventMsg.setSubscribe("subscribe".equals(event));
		mpEventMsg.setFromUser(fromUser);
		mpEventMsg.setToUser(toUser);
		mpEventMsg.setCreateTime(createTime);

		MpOutMsg mpOutMsg = ((MpEventPushHandler) msgHandler).onSubscribeEvent(mpEventMsg);
		if (mpOutMsg == null) {
			return null;
		}
		return mpOutMsg.toWxMpXmlOutMessage(inMessage);
	}

}