package org.magneton.module.geo;

import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class GeoEntry<T> {

	/**
	 * 经度
	 */
	private final double longitude;

	/**
	 * 纬度
	 */
	private final double latitude;

	/**
	 * 添加对象
	 */
	private final T member;

	public static <T> GeoEntry<T> of(double longitude, double latitude, T member) {
		return new GeoEntry<>(longitude, latitude, member);
	}

	public GeoEntry(double longitude, double latitude, T member) {
		Preconditions.checkNotNull(member);

		Preconditions.checkArgument(longitude >= -180 && longitude <= 180, "longitude must in [-180,180]");
		Preconditions.checkArgument(latitude >= -85.05112878 && latitude <= 85.05112878,
				"latitude must in [-85.05112878,85.05112878]");

		this.longitude = longitude;
		this.latitude = latitude;
		this.member = member;
	}

}