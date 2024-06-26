package org.magneton.module.wechat.open;

import com.google.common.base.Preconditions;
import org.magneton.module.wechat.core.MemoryWechatAccessTokenCache;
import org.magneton.module.wechat.core.WechatAccessTokenCache;

import javax.annotation.Nullable;

/**
 * @author zhangmsh 2022/4/2
 * @since 1.0.0
 */
public class WechatOpenBuilder {

	private final WechatOpenConfig wechatOpenConfig;

	@Nullable
	private WechatAccessTokenCache wechatAccessTokenCache = new MemoryWechatAccessTokenCache();

	private WechatOpenBuilder(WechatOpenConfig wechatOpenConfig) {
		this.wechatOpenConfig = Preconditions.checkNotNull(wechatOpenConfig);
	}

	public static WechatOpenBuilder newBuilder(WechatOpenConfig wechatOpenConfig) {
		return new WechatOpenBuilder(Preconditions.checkNotNull(wechatOpenConfig, "wechatOpenConfig"));
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