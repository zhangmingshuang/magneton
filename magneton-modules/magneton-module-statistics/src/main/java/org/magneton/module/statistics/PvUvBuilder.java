package org.magneton.module.statistics;

import com.google.common.base.Verify;
import org.magneton.module.statistics.pvpu.MemoryPvUvProcessor;
import org.magneton.module.statistics.pvpu.PvUv;
import org.magneton.module.statistics.pvpu.PvUvImpl;
import org.magneton.module.statistics.pvpu.PvUvProcessor;

/**
 * pv/uv统计.
 *
 * @author zhangmsh.
 * @since 2023.9
 */
public class PvUvBuilder {

	private PvUvBuilder() {
		// newBuilder only.
	}

	/**
	 * name of the pv/uv data
	 */
	private String name;

	/**
	 * Uv的统计周期时间，单位秒，默认1天
	 */
	private long uvTimeToLive = 24 * 60 * 60;

	/**
	 * 处理器
	 */
	private PvUvProcessor pvUvProcessor;

	public static PvUvBuilder newBuilder(String name) {
		PvUvBuilder pvUvBuilder = new PvUvBuilder();
		pvUvBuilder.name = name;
		return pvUvBuilder;
	}

	public PvUvBuilder uvTimeToLive(int uvTimeToLive) {
		Verify.verify(uvTimeToLive > 1, "uvTimeToLive must more then 1");

		this.uvTimeToLive = uvTimeToLive;
		return this;
	}

	public PvUvBuilder pvUvProcessor(PvUvProcessor pvUvProcessor) {
		this.pvUvProcessor = pvUvProcessor;
		return this;
	}

	public PvUv build() {
		if (this.pvUvProcessor == null) {
			this.pvUvProcessor = new MemoryPvUvProcessor();
		}
		return new PvUvImpl(this.name, this.uvTimeToLive, this.pvUvProcessor);
	}

}
