package org.magneton.redis.enhance.geo.query;

import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.magneton.redis.enhance.geo.GeoPosition;
import org.magneton.redis.enhance.geo.GeoUnit;

/**
 * @author zhangmsh 2022/4/10
 * @since 1.0.0
 */
@Setter
@Getter
@ToString
@Accessors(chain = true)
public class PositionArgs {

	/**
	 * KEY
	 */
	private String name;

	/**
	 * 经纬度
	 */
	private GeoPosition position;

	/**
	 * 查询距离,默认100
	 */
	private double radius = 100D;

	/**
	 * 距离单位，默认：米
	 */
	private GeoUnit unit = GeoUnit.METERS;

	/**
	 * 查看数量，默认：10
	 */
	private int count = 10;

	public static PositionArgs of(String name, double longitude, double latitude) {
		Preconditions.checkNotNull(name);
		return new PositionArgs().setName(name).setPosition(longitude, latitude);
	}

	public PositionArgs setPosition(double longitude, double latitude) {
		return this.setPosition(new GeoPosition(longitude, latitude));
	}

}
