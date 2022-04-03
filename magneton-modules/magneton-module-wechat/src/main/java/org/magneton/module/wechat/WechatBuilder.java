package org.magneton.module.wechat;

import javax.annotation.Nullable;
import org.magneton.core.base.Preconditions;
import org.magneton.module.wechat.core.oauth2.AccessTokenCache;
import org.magneton.module.wechat.core.oauth2.MemoryAccessTokenCache;

/**
 * @author zhangmsh 2022/4/2
 * @since 1.0.0
 */
public class WechatBuilder {

	private final WechatConfig wechatConfig;

	private AccessTokenCache accessTokenCache = new MemoryAccessTokenCache();

	private WechatBuilder(WechatConfig wechatConfig) {
		this.wechatConfig = Preconditions.checkNotNull(wechatConfig);
	}

	public static WechatBuilder newBuilder(WechatConfig wechatConfig) {
		return new WechatBuilder(wechatConfig);
	}

	public WechatBuilder accessTokenCache(@Nullable AccessTokenCache accessTokenCache) {
		this.accessTokenCache = accessTokenCache;
		return this;
	}

	public Wechat build() {
		return new DefaultWechatImpl(this);
	}

	protected WechatConfig getWechatConfig() {
		return this.wechatConfig;
	}

	@Nullable
	protected AccessTokenCache getAccessTokenCache() {
		return this.accessTokenCache;
	}

}
