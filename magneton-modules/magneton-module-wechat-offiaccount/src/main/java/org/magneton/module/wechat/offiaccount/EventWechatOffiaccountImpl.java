package org.magneton.module.wechat.offiaccount;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.apache.commons.lang3.StringUtils;
import org.magneton.core.Result;
import org.magneton.module.wechat.offiaccount.config.WechatOffiaccountConfig;
import org.magneton.module.wechat.offiaccount.constant.Err;
import org.magneton.module.wechat.offiaccount.core.OffiaccountContext;
import org.magneton.module.wechat.offiaccount.pojo.MessageBody;
import org.magneton.module.wechat.offiaccount.router.DefaultMessageRouter;
import org.magneton.module.wechat.offiaccount.router.MessageRouter;

/**
 * 微信公众号模块实现
 *
 * @author zhangmsh.
 * @since 2024
 */
@Slf4j
public class EventWechatOffiaccountImpl extends OffiaccountContext implements WechatOffiaccount {

	private final WechatOffiaccountConfig wechatOffiaccountConfig;

	private final MessageRouter messageRouter = new DefaultMessageRouter(this);

	private WxMpService wxService;

	public EventWechatOffiaccountImpl(WechatOffiaccountConfig wechatOffiaccountConfig) {
		this.wechatOffiaccountConfig = wechatOffiaccountConfig;

		this.init();
	}

	@Override
	public Result<String> auth(String appid, MessageBody message) {
		if (log.isDebugEnabled()) {
			log.debug("auth appid: {}, message: {}", appid, message);
		}
		String signature = message.getSignature();
		String timestamp = message.getTimestamp();
		String nonce = message.getNonce();
		String echostr = message.getEchostr();
		if (StringUtils.isAnyBlank(signature, timestamp, nonce, echostr)) {
			return Result.fail(Err.PARAMETER_INVALID);
		}
		if (!this.wxService.switchover(appid)) {
			return Result.fail(Err.APPID_INVALID.format(appid));
		}
		if (this.wxService.checkSignature(timestamp, nonce, signature)) {
			return Result.successWith(echostr);
		}
		return Result.fail(Err.PARAMETER_INVALID);
	}

	@Override
	public Result<String> dispatch(String appid, MessageBody message) {
		if (!this.wxService.switchover(appid)) {
			return Result.fail(Err.APPID_INVALID.format(appid));
		}
		String signature = message.getSignature();
		String timestamp = message.getTimestamp();
		String nonce = message.getNonce();
		String echostr = message.getEchostr();
		if (!this.wxService.checkSignature(timestamp, nonce, signature)) {
			return Result.fail(Err.SIGNATURE_INVALID);
		}
		String encryptType = message.getEncryptType();
		String requestBody = message.getRequestBody();
		if (encryptType == null || "raw".equalsIgnoreCase(encryptType)) {
			// 明文传输的消息
			WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(requestBody);
			WxMpXmlOutMessage outMessage = this.messageRouter.route(inMessage);
			if (outMessage == null) {
				return Result.successWith("");
			}
			return Result.successWith(outMessage.toXml());
		}

		if ("aes".equalsIgnoreCase(encryptType)) {
			// aes加密的消息
			WxMpXmlMessage inMessage = WxMpXmlMessage.fromEncryptedXml(requestBody,
					this.wxService.getWxMpConfigStorage(), timestamp, nonce, signature);
			log.debug("\n消息解密后内容为：\n{} ", inMessage.toString());
			WxMpXmlOutMessage outMessage = this.messageRouter.route(inMessage);
			if (outMessage == null) {
				return Result.successWith("");
			}
			return Result.successWith(outMessage.toEncryptedXml(this.wxService.getWxMpConfigStorage()));
		}
		return Result.fail(Err.ENCRYPT_TYPE_INVALID);
	}

	protected void init() {
		// 默认的配置存储
		// 可以提供自己的实现，比如在集群环境下将这些信息存储到数据库或分布式缓存里，以便各个节点能够共享access token。
		WxMpDefaultConfigImpl config = new WxMpDefaultConfigImpl();

		// 设置微信公众号的appid
		config.setAppId(Preconditions.checkNotNull(this.wechatOffiaccountConfig.getAppid(), "appId must not be null"));
		// 设置微信公众号的app corpSecret
		config.setSecret(
				Preconditions.checkNotNull(this.wechatOffiaccountConfig.getSecret(), "secret must not be null"));
		// 设置微信公众号的token
		config.setToken(Preconditions.checkNotNull(this.wechatOffiaccountConfig.getToken(), "token must not be null"));
		// 设置微信公众号的EncodingAESKey
		config.setAesKey(
				Preconditions.checkNotNull(this.wechatOffiaccountConfig.getAesKey(), "aesKey must not be null"));
		// 实际项目中请注意要保持单例，不要在每次请求时构造实例
		this.wxService = new WxMpServiceImpl();
		this.wxService.setWxMpConfigStorage(config);
	}

}