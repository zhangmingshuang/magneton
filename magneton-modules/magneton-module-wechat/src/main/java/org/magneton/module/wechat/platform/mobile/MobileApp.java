package org.magneton.module.wechat.platform.mobile;

import org.magneton.module.wechat.entity.MobileCodeReq;
import org.magneton.module.wechat.entity.MobileCodeRes;
import org.magneton.module.wechat.platform.App;

/**
 * 微信移动应用
 *
 * @author zhangmsh 2022/4/3
 * @since 1.0.0
 */
public interface MobileApp extends App {

	/**
	 * 获取Code请求数据
	 * @param mobileCodeReq Code数据
	 * @return Code请求数据
	 */
	MobileCodeRes requestCodeData(MobileCodeReq mobileCodeReq);

}
