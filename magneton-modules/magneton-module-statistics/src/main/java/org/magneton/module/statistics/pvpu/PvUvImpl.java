package org.magneton.module.statistics.pvpu;

import lombok.AllArgsConstructor;

/**
 * PVUV实现类
 *
 * @author zhangmsh.
 * @since 2023.9
 */
@AllArgsConstructor
public class PvUvImpl implements PvUv {

	/**
	 * name of the pv/uv data
	 */
	private String name;

	/**
	 * Uv的统计周期时间，单位秒
	 */
	private long uvTimeToLive;

	/**
	 * 处理器
	 */
	private PvUvProcessor pvUvProcessor;

	@Override
	public boolean isUv(String id) {
		return false;
	}

	@Override
	public boolean addIfIsUv(String id) {
		return false;
	}

}
