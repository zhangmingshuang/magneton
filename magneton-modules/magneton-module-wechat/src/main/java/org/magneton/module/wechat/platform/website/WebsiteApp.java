package org.magneton.module.wechat.platform.website;

import org.magneton.module.wechat.entity.WebsiteCodeReq;
import org.magneton.module.wechat.platform.App;

/**
 * 微信网站应用
 *
 * @author zhangmsh 2022/4/2
 * @since 1.0.0
 */
public interface WebsiteApp extends App {

	/**
	 * 生成请求Code的URL
	 *
	 * @apiNote 第三方使用网站应用授权登录前请注意已获取相应网页授权作用域（scope=snsapi_login），则可以通过在PC端打开以下链接：
	 * https://open.weixin.qq.com/connect/qrconnect?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect
	 * 若提示“该链接无法访问”，请检查参数是否填写错误，如redirect_uri的域名与审核时填写的授权域名不一致或scope不为snsapi_login。
	 *
	 * 用户允许授权后，将会重定向到redirect_uri的网址上，并且带上code和state参数
	 * @return 请求Code的URL地址
	 */
	String requestCodeUrl(WebsiteCodeReq websiteCodeReq);

}
