package org.magneton.module.wechat.miniprogram;

import com.google.common.base.Preconditions;
import org.magneton.core.Consequences;
import org.magneton.module.wechat.core.Req;
import org.magneton.module.wechat.miniprogram.entity.Code2Session;

/**
 * @author zhangmsh 2022/5/1
 * @since 1.0.0
 */
public class DefaultWechatMiniProgram implements WechatMiniProgram {

	private final WechatMiniProgramConfig config;

	public DefaultWechatMiniProgram(WechatMiniProgramConfig config) {
		Preconditions.checkNotNull(config);
		Preconditions.checkNotNull(config.getAppid(), "appid must not be null");
		Preconditions.checkNotNull(config.getSecret(), "secret must not be null");
		this.config = config;
	}

	@Override
	public Consequences<Code2Session> code2Session(String code) {
		Preconditions.checkNotNull(code, "code must not be null");
		String url = String.format(
				"https://api.weixin.qq.com/sns/jscode2session?"
						+ "appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
				this.config.getAppid(), this.config.getSecret(), code);
		return Req.doGet(url, Code2Session.class);
	}

}
