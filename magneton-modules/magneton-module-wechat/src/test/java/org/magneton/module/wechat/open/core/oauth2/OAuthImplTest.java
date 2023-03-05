package org.magneton.module.wechat.open.core.oauth2;

import com.alibaba.testable.core.annotation.MockInvoke;
import com.alibaba.testable.core.model.MockScope;
import org.magneton.core.Reply;
import org.magneton.module.wechat.core.Req;
import org.magneton.module.wechat.open.entity.AccessTokenRes;

/**
 * @author zhangmsh 2022/4/3
 * @since 1.0.0
 */
class OAuthImplTest {

	public static class Mock {

		@MockInvoke(targetClass = Req.class, scope = MockScope.GLOBAL)
		public static <T> Reply<T> doGet(String requestUrl, Class<T> clazz) {
			AccessTokenRes accessTokenRes = new AccessTokenRes();
			accessTokenRes.setAccess_token("testAccessToken");
			accessTokenRes.setExpires_in(0);
			accessTokenRes.setRefresh_token("testRefreshToken");
			accessTokenRes.setOpenid("testOpenId");
			accessTokenRes.setScope("testScope");
			return Reply.success(accessTokenRes).coverage();
		}

	}

}