package org.magneton.module.wechat;

import javax.annotation.Nullable;

import org.magneton.core.base.Preconditions;
import org.magneton.module.wechat.core.oauth2.MemoryWechatAccessTokenCache;
import org.magneton.module.wechat.core.oauth2.WechatAccessTokenCache;

/**
 * @author zhangmsh 2022/4/2
 * @since 1.0.0
 */
public class WechatBuilder {

	private final WechatConfig wechatConfig;

	private WechatAccessTokenCache wechatAccessTokenCache = new MemoryWechatAccessTokenCache();

	private WechatBuilder(WechatConfig wechatConfig) {
		this.wechatConfig = Preconditions.checkNotNull(wechatConfig);
	}

	public static WechatBuilder newBuilder(WechatConfig wechatConfig) {
		return new WechatBuilder(wechatConfig);
	}

	public WechatBuilder accessTokenCache(@Nullable WechatAccessTokenCache wechatAccessTokenCache) {
		this.wechatAccessTokenCache = wechatAccessTokenCache;
		return this;
	}

	public Wechat build() {
		return new DefaultWechatImpl(this);
	}

	protected WechatConfig getWechatConfig() {
		return this.wechatConfig;
	}

	@Nullable
	protected WechatAccessTokenCache getAccessTokenCache() {
		return this.wechatAccessTokenCache;
	}

}
