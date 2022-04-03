package org.magneton.module.wechat.platform.website;

import org.magneton.core.base.MoreObjects;
import org.magneton.core.base.Preconditions;
import org.magneton.core.base.Strings;
import org.magneton.module.wechat.WechatContext;
import org.magneton.module.wechat.entity.WebsiteCodeReq;
import org.magneton.module.wechat.platform.AbstractApp;

/**
 * @author zhangmsh 2022/4/2
 * @since 1.0.0
 */
public class DefaultWebsiteAppImpl extends AbstractApp implements WebsiteApp {

	public DefaultWebsiteAppImpl(WechatContext wechatContext) {
		super(wechatContext);
	}

	@Override
	public String requestCodeUrl(WebsiteCodeReq websiteCodeReq) {
		Preconditions.checkNotNull(websiteCodeReq);
		return Strings.format(
				"https://open.weixin.qq.com/connect/qrconnect?appid=%s"
						+ "&redirect_uri=%s&response_type=code&scope=%s&state=%s#wechat_redirect",
				super.getWechatContext().getWechatConfig().getAppid(), websiteCodeReq.getRedirect_uri(),
				websiteCodeReq.getScope(), MoreObjects.firstNonNull(websiteCodeReq.getState(), ""));

	}

}
