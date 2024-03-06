package org.magneton.module.statistics.pvpu;

import org.redisson.api.RBitSet;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * 基于Redisson的PVUV处理器
 *
 * @author zhangmsh 18/03/2022
 * @since 2.0.7
 */
public class RedissonPvUv extends AbstractPvUv {

	private static final String KEY = "mton:m:stat";

	private final RedissonClient redissonClient;

	public RedissonPvUv(RedissonClient redissonClient, PvUvConfig pvUvConfig) {
		super(pvUvConfig);
		this.redissonClient = redissonClient;
	}

	@Override
	public Uv uv(String group) {
		return new RedissonUv(this.getPvUvConfig(), group, this.redissonClient);
	}

	public static class RedissonUv implements Uv {

		private final PvUvConfig pvUvConfig;

		private final String group;

		private final RedissonClient redissonClient;

		private final RBitSet bucket;

		public RedissonUv(PvUvConfig pvUvConfig, String group, RedissonClient redissonClient) {
			this.pvUvConfig = pvUvConfig;
			this.group = group;
			this.redissonClient = redissonClient;
			this.bucket = this.getBucket();

			if (pvUvConfig.getUvStatsCycleSec() > 0) {
				if (!this.bucket.isExists()) {
					this.bucket.set(0);
				}
				this.bucket.expire(pvUvConfig.getUvStatsCycleSec(), TimeUnit.SECONDS);
				this.bucket.clear(0);
			}
		}

		@Override
		public boolean is(long id) {
			return !this.bucket.get(id);
		}

		@Override
		public boolean set(long id) {
			return !this.bucket.set(id);
		}

		@Override
		public boolean remove(long id) {
			return this.bucket.clear(id);
		}

		@Override
		public void clean() {
			this.bucket.delete();
		}

		private RBitSet getBucket() {
			return this.redissonClient.getBitSet(this.key());
		}

		/**
		 * 获取key
		 * @return 预期的key
		 */
		private String key() {
			return KEY + ":uv:" + this.group;
		}

	}

}