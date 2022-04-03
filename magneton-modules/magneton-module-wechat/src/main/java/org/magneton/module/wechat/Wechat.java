package org.magneton.module.wechat;

import org.magneton.module.wechat.platform.mobile.MobileApp;
import org.magneton.module.wechat.platform.website.WebsiteApp;

/**
 * @author zhangmsh 2022/4/2
 * @since 1.0.0
 */
public interface Wechat {

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
