package org.magneton.module.wechat.open;

import com.google.common.base.Preconditions;
import org.magneton.module.wechat.open.core.oauth2.WechatOAuth;
import org.magneton.module.wechat.open.core.oauth2.WechatOAuthImpl;
import org.magneton.module.wechat.open.platform.mobile.DefaultMobileOAppImpl;
import org.magneton.module.wechat.open.platform.mobile.MobileOApp;
import org.magneton.module.wechat.open.platform.website.DefaultWebsiteOAppImpl;
import org.magneton.module.wechat.open.platform.website.WebsiteOApp;

/**
 * 微信开放平台
 *
 * @author zhangmsh 2022/4/2
 * @since 1.0.0
 */
public class DefaultWechatOpenImpl implements WechatOpen, WechatOpenContext {

	private final WechatOpenConfig wechatOpenConfig;

	private WebsiteOApp WebsiteOApp;

	private MobileOApp mobileApp;

	private WechatOAuth wechatOAuth;

	public DefaultWechatOpenImpl(WechatOpenBuilder wechatOpenBuilder) {
		this.wechatOpenConfig = Preconditions.checkNotNull(wechatOpenBuilder.getWechatConfig());
		this.init(wechatOpenBuilder);
	}

	protected void init(WechatOpenBuilder wechatOpenBuilder) {
		this.wechatOAuth = new WechatOAuthImpl(this.wechatOpenConfig, wechatOpenBuilder.getAccessTokenCache());
		this.WebsiteOApp = new DefaultWebsiteOAppImpl(this);
		this.mobileApp = new DefaultMobileOAppImpl(this);
	}

	@Override
	public WebsiteOApp website() {
		return this.WebsiteOApp;
	}

	@Override
	public MobileOApp mobile() {
		return this.mobileApp;
	}

	@Override
	public WechatOpenConfig getWechatConfig() {
		return this.wechatOpenConfig;
	}

	@Override
	public WechatOAuth getOAuth() {
		return this.wechatOAuth;
	}

}