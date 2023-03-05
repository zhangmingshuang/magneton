package org.magneton.module.wechat.open.platform.mobile;

import com.google.common.base.Preconditions;
import org.magneton.module.wechat.open.WechatOpenContext;
import org.magneton.module.wechat.open.entity.MobileCodeReq;
import org.magneton.module.wechat.open.entity.MobileCodeRes;
import org.magneton.module.wechat.open.platform.AbstractOApp;

/**
 * @author zhangmsh 2022/4/3
 * @since 1.0.0
 */
public class DefaultMobileOAppImpl extends AbstractOApp implements MobileOApp {

	public DefaultMobileOAppImpl(WechatOpenContext wechatOpenContext) {
		super(wechatOpenContext);
	}

	@Override
	public MobileCodeRes requestCodeData(MobileCodeReq mobileCodeReq) {
		Preconditions.checkNotNull(mobileCodeReq);
		return new MobileCodeRes().setAppid(super.getWechatContext().getWechatConfig().getAppid())
				.setScope(mobileCodeReq.getScope()).setState(mobileCodeReq.getState());
	}

}
