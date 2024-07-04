package org.magneton.enhance.wechat.mp.core;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.apache.commons.lang3.StringUtils;
import org.magneton.core.Result;
import org.magneton.enhance.wechat.mp.WechatMpTemplate;
import org.magneton.enhance.wechat.mp.config.WechatMpConfig;
import org.magneton.enhance.wechat.mp.constant.Err;
import org.magneton.enhance.wechat.mp.core.asset.AssetManagement;
import org.magneton.enhance.wechat.mp.core.asset.MaterialAssetManagement;
import org.magneton.enhance.wechat.mp.core.menu.MenuManagement;
import org.magneton.enhance.wechat.mp.core.menu.MpMenuManagement;
import org.magneton.enhance.wechat.mp.core.message.pojo.MpMsgBody;
import org.magneton.enhance.wechat.mp.core.router.DefaultDispatchRouter;
import org.magneton.enhance.wechat.mp.core.router.DispatchRouter;

/**
 * 微信公众号模块实现
 *
 * @author zhangmsh.
 * @since 2024
 */
@Slf4j
public class EventWechatMpTemplate implements WechatMpTemplate {

	private final WechatMpConfig wechatMpConfig;

	private final DispatchRouter dispatchRouter = new DefaultDispatchRouter();

	private WxMpService wxService;

	private Context context;

	public EventWechatMpTemplate(WechatMpConfig wechatMpConfig) {
		this.wechatMpConfig = wechatMpConfig;

		this.init();
	}

	@Override
	public Result<String> auth(String appid, MpMsgBody message) {
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
	public Result<String> dispatch(String appid, MpMsgBody message) {
		if (!this.wxService.switchover(appid)) {
			return Result.fail(Err.APPID_INVALID.format(appid));
		}
		MpContext.setCurrentAppid(appid);

		String signature = message.getSignature();
		String timestamp = message.getTimestamp();
		String nonce = message.getNonce();
		String echostr = message.getEchostr();

		if (this.wechatMpConfig.isSignCheck() && !this.wxService.checkSignature(timestamp, nonce, signature)) {
			return Result.fail(Err.SIGNATURE_INVALID);
		}

		String encryptType = message.getEncryptType();
		String requestBody = message.getRequestBody();
		if (encryptType == null || "raw".equalsIgnoreCase(encryptType)) {
			// 明文传输的消息
			WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(requestBody);
			WxMpXmlOutMessage outMessage = this.dispatchRouter.dispatch(inMessage);
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
			WxMpXmlOutMessage outMessage = this.dispatchRouter.dispatch(inMessage);
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
		config.setAppId(Preconditions.checkNotNull(this.wechatMpConfig.getAppid(), "appId must not be null"));
		// 设置微信公众号的app corpSecret
		config.setSecret(Preconditions.checkNotNull(this.wechatMpConfig.getSecret(), "secret must not be null"));
		// 设置微信公众号的token
		config.setToken(Preconditions.checkNotNull(this.wechatMpConfig.getToken(), "token must not be null"));
		// 设置微信公众号的EncodingAESKey
		config.setAesKey(Preconditions.checkNotNull(this.wechatMpConfig.getAesKey(), "aesKey must not be null"));
		// 实际项目中请注意要保持单例，不要在每次请求时构造实例
		this.wxService = new WxMpServiceImpl();
		this.wxService.setWxMpConfigStorage(config);

		this.context = new Context(this.wxService);
	}

	// =============================== 素材管理 ===============================

	@Override
	public AssetManagement assetManagement(String appid) {
		if (!this.wxService.switchover(appid)) {
			throw new IllegalArgumentException(Err.APPID_INVALID.format(appid).message());
		}
		return this.context.getAssetManagement();
	}

	@Override
	public MenuManagement menuManagement(String appid) {
		if (!this.wxService.switchover(appid)) {
			throw new IllegalArgumentException(Err.APPID_INVALID.format(appid).message());
		}
		return this.context.getMeneManagement();
	}

	private static class Context {

		private final WxMpService wxService;

		private AssetManagement assetManagement;

		private MenuManagement menuManagement;

		public Context(WxMpService wxService) {
			this.wxService = wxService;
		}

		public AssetManagement getAssetManagement() {
			if (this.assetManagement == null) {
				synchronized (Context.class) {
					if (this.assetManagement == null) {
						this.assetManagement = new MaterialAssetManagement(this.wxService);
					}
				}
			}
			return this.assetManagement;
		}

		public MenuManagement getMeneManagement() {
			if (this.menuManagement == null) {
				synchronized (Context.class) {
					if (this.menuManagement == null) {
						this.menuManagement = new MpMenuManagement(this.wxService);
					}
				}
			}
			return this.menuManagement;
		}

	}

}