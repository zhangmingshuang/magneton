package org.magneton.module.wechat.open;

import org.magneton.module.wechat.open.platform.mobile.MobileApp;
import org.magneton.module.wechat.open.platform.website.WebsiteApp;

/**
 * @author zhangmsh 2022/4/2
 * @since 1.0.0
 */
public interface WechatOpen {

	/**
	 * 网站应用
	 * @return 微信网站应用处理器
	 */
	WebsiteApp website();

	/**
	 * 移动应用
	 * @return 微信移动应用处理器
	 */
	MobileApp mobile();

}
