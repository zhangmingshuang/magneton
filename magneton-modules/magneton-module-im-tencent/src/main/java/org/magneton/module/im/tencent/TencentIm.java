package org.magneton.module.im.tencent;

import com.google.common.base.Preconditions;
import com.tencentyun.TLSSigAPIv2;
import javax.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;
import org.magneton.module.im.tencent.api.msg.Msg;
import org.magneton.module.im.tencent.api.msg.MsgImpl;

/**
 * .
 *
 * @author zhangmsh 2022/4/26
 * @since 2.0.8
 */
public class TencentIm {

	@Getter
	private final TencentImConfig config;

	@Nullable
	private UserSignCache userSignCache;

	@Getter
	private String adminUserSign;

	private TLSSigAPIv2 api;

	@Setter
	private Msg msg = new MsgImpl(this);

	public TencentIm(TencentImConfig config) {
		this.config = config;
		this.init();
	}

	public Msg msg() {
		return this.msg;
	}

	protected void init() {
		this.api = new TLSSigAPIv2(this.config.getAppId(), Preconditions.checkNotNull(this.config.getAppSecret()));
		this.adminUserSign = this.genSig(this.config.getAdmin());
	}

	public TLSSigAPIv2 getApi() {
		return this.api;
	}

	public void cleanSig(String userId) {
		if (this.userSignCache != null) {
			this.userSignCache.remove(userId);
		}
	}

	/**
	 * UserSig 是用户登录即时通信 IM 的密码，其本质是对 UserID 等信息加密后得到的密文
	 * @param userId 用户 ID
	 * @param expireSeconds UserSig 的有效期，单位为秒。最小值为3600
	 * @return UserSig
	 */
	public String genSig(String userId, long expireSeconds) {
		Preconditions.checkNotNull(userId);
		String userSig = null;
		if (this.userSignCache != null) {
			userSig = this.userSignCache.get(userId);
		}
		if (userSig == null) {
			userSig = this.api.genUserSig(userId, Math.max(3600, expireSeconds));
			if (this.userSignCache != null) {
				this.userSignCache.put(userId, userSig, expireSeconds);
			}
		}
		return userSig;
	}

	/**
	 * UserSig 是用户登录即时通信 IM 的密码，其本质是对 UserID 等信息加密后得到的密文
	 * @param userId 用户 ID
	 * @return UserSig
	 */
	public String genSig(String userId) {
		Preconditions.checkNotNull(userId);
		return this.api.genUserSig(userId, this.config.getUserSignExpireSeconds());
	}

	public void setUserSignCache(@Nullable UserSignCache userSignCache) {
		this.userSignCache = userSignCache;
	}

}
