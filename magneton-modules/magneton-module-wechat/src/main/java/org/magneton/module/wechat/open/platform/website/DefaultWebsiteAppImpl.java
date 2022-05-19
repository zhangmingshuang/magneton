package org.magneton.module.wechat.open.platform.website;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import org.magneton.module.wechat.open.WechatContext;
import org.magneton.module.wechat.open.entity.WebsiteCodeReq;
import org.magneton.module.wechat.open.platform.AbstractApp;

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
