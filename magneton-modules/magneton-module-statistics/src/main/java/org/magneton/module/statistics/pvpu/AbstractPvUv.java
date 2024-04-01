package org.magneton.module.statistics.pvpu;

import lombok.Getter;

/**
 * PVUV.
 *
 * @author zhangmsh.
 * @since M2023.9
 */
public abstract class AbstractPvUv implements PvUv {

	@Getter
	private final PvUvConfig pvUvConfig;

	public AbstractPvUv(PvUvConfig pvUvConfig) {
		this.pvUvConfig = pvUvConfig;
	}

}