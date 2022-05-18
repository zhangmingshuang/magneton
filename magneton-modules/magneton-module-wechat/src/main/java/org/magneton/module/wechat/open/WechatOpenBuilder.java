package org.magneton.module.wechat.open;

import javax.annotation.Nullable;
import org.magneton.core.base.Preconditions;
import org.magneton.module.wechat.open.core.oauth2.MemoryWechatAccessTokenCache;
import org.magneton.module.wechat.open.core.oauth2.WechatAccessTokenCache;

/**
 * @author zhangmsh 2022/4/2
 * @since 1.0.0
 */
public class WechatOpenBuilder {

	private final WechatOpenConfig wechatOpenConfig;

	private WechatAccessTokenCache wechatAccessTokenCache = new MemoryWechatAccessTokenCache();

	private WechatOpenBuilder(WechatOpenConfig wechatOpenConfig) {
		this.wechatOpenConfig = Preconditions.checkNotNull(wechatOpenConfig);
	}

	public static WechatOpenBuilder newBuilder(WechatOpenConfig wechatOpenConfig) {
		return new WechatOpenBuilder(wechatOpenConfig);
	}

	public WechatOpenBuilder accessTokenCache(@Nullable WechatAccessTokenCache wechatAccessTokenCache) {
		this.wechatAccessTokenCache = wechatAccessTokenCache;
		return this;
	}

	public WechatOpen build() {
		return new DefaultWechatOpenImpl(this);
	}

	protected WechatOpenConfig getWechatConfig() {
		return this.wechatOpenConfig;
	}

	@Nullable
	protected WechatAccessTokenCache getAccessTokenCache() {
		return this.wechatAccessTokenCache;
	}

}
