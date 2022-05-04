package org.magneton.module.wechat.open.platform.mobile;

import org.magneton.core.base.Preconditions;
import org.magneton.module.wechat.open.WechatContext;
import org.magneton.module.wechat.open.entity.MobileCodeReq;
import org.magneton.module.wechat.open.entity.MobileCodeRes;
import org.magneton.module.wechat.open.platform.AbstractApp;

/**
 * @author zhangmsh 2022/4/3
 * @since 1.0.0
 */
public class DefaultMobileAppImpl extends AbstractApp implements MobileApp {

	public DefaultMobileAppImpl(WechatContext wechatContext) {
		super(wechatContext);
	}

	@Override
	public MobileCodeRes requestCodeData(MobileCodeReq mobileCodeReq) {
		Preconditions.checkNotNull(mobileCodeReq);
		return new MobileCodeRes().setAppid(super.getWechatContext().getWechatConfig().getAppid())
				.setScope(mobileCodeReq.getScope()).setState(mobileCodeReq.getState());
	}

}
