package org.magneton.module.core.wechat;

import org.magneton.core.base.Preconditions;

/**
 * 微信上下文，用来共享相关的微信数据。
 *
 * @author zhangmsh 2022/4/1
 * @since 1.0.0
 */
public class WechatContext {

	private static final WechatContext INSTANCE = new WechatContext();

	private WechatTokenCache wechatTokenCache = new MemoryWechatTokenCache();

	private WechatContext() {

	}

	public WechatAccessToken getCachedAccessToken(String code) {
		this.validate();
		Preconditions.checkNotNull(code);
		return this.wechatTokenCache.get(code);
	}

	public void setCachedAccessToken(String code, WechatAccessToken wechatAccessToken) {
		this.validate();
		Preconditions.checkNotNull(code);
		this.wechatTokenCache.set(code, wechatAccessToken);
	}

	private void validate() {
		Preconditions.checkNotNull(this.wechatTokenCache, "WechatTokenCache must not be null");
	}

	public void setWechatTokenCache(WechatTokenCache wechatTokenCache) {
		this.wechatTokenCache = Preconditions.checkNotNull(wechatTokenCache);
	}

}
