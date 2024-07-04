package org.magneton.enhance.pvuv;

import java.util.BitSet;

/****
 * 基于内存的PVUV处理器**
 *
 * @author zhangmsh.
 * @since 2023.9
 */
public class MemoryPvUv extends AbstractPvUv {

	public MemoryPvUv(PvUvConfig pvUvConfig) {
		super(pvUvConfig);
	}

	@Override
	public Uv uv(String group) {
		return new MemoryUv(this.getPvUvConfig(), group);
	}

	public static class MemoryUv implements Uv {

		private final PvUvConfig pvUvConfig;

		private final String group;

		private final ExpireableBitSet bucket;

		public MemoryUv(PvUvConfig pvUvConfig, String group) {
			this.pvUvConfig = pvUvConfig;
			this.group = group;

			this.bucket = new ExpireableBitSet(new BitSet(), pvUvConfig.getUvStatsCycleSec());
		}

		@Override
		public boolean is(long id) {
			if (this.bucket.isExpire()) {
				this.clean();
			}
			return !this.bucket.bitSet.get((int) id);
		}

		@Override
		public boolean set(long id) {
			if (this.is(id)) {
				this.bucket.bitSet.set((int) id);
				return true;
			}
			return false;
		}

		@Override
		public boolean remove(long id) {
			boolean isUv = !this.is(id);
			this.bucket.bitSet.clear((int) id);
			return isUv;
		}

		@Override
		public void clean() {
			this.bucket.bitSet.clear();
		}

	}

	public static class ExpireableBitSet {

		private final BitSet bitSet;

		private final long uvStatsCycleSec;

		private long expireTimeMills;

		public ExpireableBitSet(BitSet bitSet, long uvStatsCycleSec) {
			this.bitSet = bitSet;
			this.uvStatsCycleSec = uvStatsCycleSec;
			if (uvStatsCycleSec > 0) {
				this.expireTimeMills = System.currentTimeMillis() + uvStatsCycleSec * 1000;
			}
		}

		public boolean isExpire() {
			return this.expireTimeMills > 0 && System.currentTimeMillis() > this.expireTimeMills;
		}

	}

}