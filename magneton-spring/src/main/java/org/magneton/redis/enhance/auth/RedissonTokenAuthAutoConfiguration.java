package org.magneton.redis.enhance.auth;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.hash.Hashing;
import org.magneton.enhance.auth.TokenAuth;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 基于Redisson实现的TokenAuth
 *
 * @author zhangmsh.
 * @since 1.0.0
 */
@Component
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({ RedissonClient.class, TokenAuth.class })
public class RedissonTokenAuthAutoConfiguration {

	private static final String TOKEN_AUTH_KEY = MoreObjects.firstNonNull(System.getProperty("magneton.auth.token"),
			"M_AUTH_TOKEN");

	@Bean
	@ConditionalOnMissingBean
	public TokenAuth tokenAuth(RedissonClient redissonClient) {
		return new RedissonTokenAuth(redissonClient);
	}

	@SuppressWarnings("unchecked")
	public static class RedissonTokenAuth implements TokenAuth {

		private RedissonClient client;

		public RedissonTokenAuth(RedissonClient redissonClient) {
			this.client = redissonClient;
		}

		@Override
		public <A> String login(A obj) {
			Preconditions.checkNotNull(obj, "obj");
			String token = Hashing.sha256().hashString(UUID.randomUUID().toString(), StandardCharsets.UTF_8).toString();
			RBucket<Object> bucket = this.client.getBucket(token);
			bucket.set(obj, 1, TimeUnit.HOURS);
			return token;
		}

		@Nullable
		@Override
		public <A> A login(String token, A obj) {
			Preconditions.checkNotNull(token, "token");
			Preconditions.checkNotNull(obj, "obj");
			RBucket<Object> bucket = this.client.getBucket(token);
			Object oldVal = bucket.get();
			bucket.set(obj, 1, TimeUnit.HOURS);
			try {
				return (A) oldVal;
			}
			catch (Exception e) {
				return null;
			}
		}

		@Override
		public boolean isLogin(String token) {
			Preconditions.checkNotNull(token, "token");
			return this.client.getBucket(token).isExists();
		}

		@Nullable
		@Override
		public <A> A getLogin(String token) {
			Preconditions.checkNotNull(token, "token");
			RBucket<Object> bucket = this.client.getBucket(token);
			try {
				return (A) bucket.get();
			}
			catch (Exception e) {
				return null;
			}
		}

	}

}
