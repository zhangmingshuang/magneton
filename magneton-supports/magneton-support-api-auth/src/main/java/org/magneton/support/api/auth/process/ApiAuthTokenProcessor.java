package org.magneton.support.api.auth.process;

import javax.annotation.Nullable;
import org.magneton.foundation.util.Pair;

/**
 * 授权Token处理器
 *
 * @author zhangmsh 2022/3/19
 * @since 1.0.0
 */
public interface ApiAuthTokenProcessor {

	/**
	 * 创建授权Token
	 * @param mobile 手机号
	 * @return 授权Token，first=操作授权Token， second=自动登录Token。如果返回为{@code null}，则使用默认策略生成。
	 */
	@Nullable
	Pair<String, String> createToken(String mobile);

}
