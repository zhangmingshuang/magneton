package org.magneton.module.im.tencent;

import javax.annotation.Nullable;

/**
 * .
 *
 * @author zhangmsh 2022/4/26
 * @since 2.0.8
 */
public interface UserSignCache {

	void put(String userId, String userSig, long expireSeconds);

	@Nullable
	String get(String userId);

	void remove(String userId);

}
