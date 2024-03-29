package org.magneton.module.wechat.open.platform.website;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import org.magneton.module.wechat.open.WechatOpenContext;
import org.magneton.module.wechat.open.entity.WebsiteCodeReq;
import org.magneton.module.wechat.open.platform.AbstractOApp;

/**
 * @author zhangmsh 2022/4/2
 * @since 1.0.0
 */
public class DefaultWebsiteOAppImpl extends AbstractOApp implements WebsiteOApp {

	public DefaultWebsiteOAppImpl(WechatOpenContext wechatOpenContext) {
		super(wechatOpenContext);
	}

	@Override
	public String requestCodeUrl(WebsiteCodeReq websiteCodeReq) {
		Preconditions.checkNotNull(websiteCodeReq);
		//@formatter:off
		return String.format(
				"https://open.weixin.qq.com/connect/qrconnect?appid=%s"
						+ "&redirect_uri=%s&response_type=code&scope=%s&state=%s#wechat_redirect",
				super.getWechatContext().getWechatConfig().getAppid(),
				websiteCodeReq.getRedirectUri(),
				websiteCodeReq.getScope(),
				MoreObjects.firstNonNull(websiteCodeReq.getState(), ""));
		//@formatter:on
	}

}
