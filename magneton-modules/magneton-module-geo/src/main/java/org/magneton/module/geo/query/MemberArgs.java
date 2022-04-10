package org.magneton.module.geo.query;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.magneton.core.base.Preconditions;
import org.magneton.module.geo.GeoUnit;

/**
 * @author zhangmsh 2022/4/10
 * @since 1.0.0
 */
@Setter
@Getter
@ToString
@Accessors(chain = true)
public class MemberArgs<V> {

	/**
	 * KEY
	 */
	private String name;

	/**
	 * 查询的数据
	 */
	private V member;

	/**
	 * 查询距离，默认100
	 */
	private double radius = 100;

	/**
	 * 距离单位，默认：米
	 */
	private GeoUnit unit = GeoUnit.METERS;

	/**
	 * 查看数量，默认：10
	 */
	private int count = 10;

	public static <V> MemberArgs of(String name, V member) {
		Preconditions.checkNotNull(name);
		Preconditions.checkNotNull(member);
		return new MemberArgs().setName(name).setMember(member);
	}

}
