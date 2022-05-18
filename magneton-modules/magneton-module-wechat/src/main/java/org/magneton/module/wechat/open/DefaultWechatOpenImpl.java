package org.magneton.module.wechat.open;

import org.magneton.core.base.Preconditions;
import org.magneton.module.wechat.open.core.oauth2.WechatOAuth;
import org.magneton.module.wechat.open.core.oauth2.WechatOAuthImpl;
import org.magneton.module.wechat.open.platform.mobile.DefaultMobileAppImpl;
import org.magneton.module.wechat.open.platform.mobile.MobileApp;
import org.magneton.module.wechat.open.platform.website.DefaultWebsiteAppImpl;
import org.magneton.module.wechat.open.platform.website.WebsiteApp;

/**
 * @author zhangmsh 2022/4/2
 * @since 1.0.0
 */
public class DefaultWechatOpenImpl implements WechatOpen, WechatContext {

	private final WechatOpenConfig wechatOpenConfig;

	private WebsiteApp websiteApp;

	private MobileApp mobileApp;

	private WechatOAuth wechatOAuth;

	public DefaultWechatOpenImpl(WechatOpenBuilder wechatOpenBuilder) {
		this.wechatOpenConfig = Preconditions.checkNotNull(wechatOpenBuilder.getWechatConfig());
		this.init(wechatOpenBuilder);
	}

	protected void init(WechatOpenBuilder wechatOpenBuilder) {
		this.wechatOAuth = new WechatOAuthImpl(this.wechatOpenConfig, wechatOpenBuilder.getAccessTokenCache());
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
	public WechatOpenConfig getWechatConfig() {
		return this.wechatOpenConfig;
	}

	@Override
	public WechatOAuth getOAuth() {
		return this.wechatOAuth;
	}

}
