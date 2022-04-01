package org.magneton.module.core.wechat;

/**
 * @author zhangmsh 2022/4/2
 * @since 1.0.0
 */
public class MemoryWechatTokenCache implements WechatTokenCache {

	@Override
	public WechatAccessToken get(String code) {
		return null;
	}

	@Override
	public void set(String code, WechatAccessToken wechatAccessToken) {

	}

}
