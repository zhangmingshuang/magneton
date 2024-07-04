package org.magneton.enhance.pvuv;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * PvUv 配置
 *
 * @author zhangmsh.
 * @since M2023.9
 */
@Setter
@Getter
@ToString
public class PvUvConfig {

	/**
	 * Uv的统计周期时间，单位秒，默认1天
	 */
	private long uvStatsCycleSec = 24 * 60 * 60;

}