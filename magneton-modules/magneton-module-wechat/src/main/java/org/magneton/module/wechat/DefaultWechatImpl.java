package org.magneton.module.wechat;

import org.magneton.core.base.Preconditions;
import org.magneton.module.wechat.core.oauth2.OAuth;
import org.magneton.module.wechat.core.oauth2.OAuthImpl;
import org.magneton.module.wechat.platform.mobile.DefaultMobileAppImpl;
import org.magneton.module.wechat.platform.mobile.MobileApp;
import org.magneton.module.wechat.platform.website.DefaultWebsiteAppImpl;
import org.magneton.module.wechat.platform.website.WebsiteApp;

/**
 * @author zhangmsh 2022/4/2
 * @since 1.0.0
 */
public class DefaultWechatImpl implements Wechat, WechatContext {

	private final WechatConfig wechatConfig;

	private WebsiteApp websiteApp;

	private MobileApp mobileApp;

	private OAuth oAuth;

	public DefaultWechatImpl(WechatBuilder wechatBuilder) {
		this.wechatConfig = Preconditions.checkNotNull(wechatBuilder.getWechatConfig());
		this.init(wechatBuilder);
	}

	protected void init(WechatBuilder wechatBuilder) {
		this.oAuth = new OAuthImpl(this.wechatConfig, wechatBuilder.getAccessTokenCache());
		this.websiteApp = new DefaultWebsiteAppImpl(this);
		this.mobileApp = new DefaultMobileAppImpl(this);
	}

	@Override
	public WebsiteApp website() {
		return this.websiteApp;
	}

	@Override
	public MobileApp mobile() {
		return this.mobileApp;
	}

	@Override
	public WechatConfig getWechatConfig() {
		return this.wechatConfig;
	}

	@Override
	public OAuth getOAuth() {
		return this.oAuth;
	}

}
