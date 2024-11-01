package org.magneton.spring.comp;

import com.google.common.base.Preconditions;
import com.google.common.hash.Hashing;
import org.magneton.cache.MCache;
import org.magneton.enhance.auth.MTokenAuth;
import org.magneton.spring.cache.MCacheConditional;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * 基于缓存的Token认证.
 *
 * @author zhangmsh
 * @since 2024
 */
@Component
@MCacheConditional
public class CachedTokenAuth implements MTokenAuth {

	private final MCache cache;

	public CachedTokenAuth(MCache cache) {
		this.cache = cache;
	}

	@Override
	public <A> String login(A obj, int keepTime) {
		Preconditions.checkNotNull(obj, "obj");
		String token = Hashing.sha256().hashString(UUID.randomUUID().toString(), StandardCharsets.UTF_8).toString();
		this.cache.set(token, obj, keepTime);
		return token;
	}

	@Nullable
	@Override
	public <A> A login(String token, A obj, int keepTime) {
		Preconditions.checkNotNull(token, "token");
		Preconditions.checkNotNull(obj, "obj");
		A a = (A) this.cache.get(token, obj.getClass());
		this.cache.set(token, obj, keepTime);
		return a;
	}

	@Override
	public boolean isLogin(String token) {
		if (this.cache.exist(token)) {
			this.cache.expire(token, 3600);
			return true;
		}
		return false;
	}

	@Nullable
	@Override
	public <A> A getLogin(String token, Class<A> clazz) {
		return this.cache.get(token, clazz);
	}

	@Override
	public void logout(String token) {
		this.cache.del(token);
	}

}
