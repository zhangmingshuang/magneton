package org.magneton.cache.redis.enhance.geo;

/**
 * .
 *
 * @author zhangmsh 2022/2/11
 * @since 1.2.0
 */
public enum GeoUnit {

	/**
	 * 米
	 */
	METERS("m"),
	/**
	 * 公里
	 */
	KILOMETERS("km"),
	/**
	 * 英里
	 */
	MILES("mi"),
	/**
	 * 英尺，1ft= 0.3048000m
	 *
	 * m =ft/3.2808
	 */
	FEET("ft");

	private final String unit;

	GeoUnit(String unit) {
		this.unit = unit;
	}

	public String unit() {
		return this.unit;
	}

}
