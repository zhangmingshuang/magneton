package org.magneton.redis.enhance.geo.query;

import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.magneton.redis.enhance.geo.GeoUnit;

/**
 * @author zhangmsh 2022/4/10
 * @since 1.0.0
 */
@Setter
@Getter
@ToString
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

	public static <V> MemberArgs<V> of(String name, V member) {
		Preconditions.checkNotNull(name, "name is null");
		Preconditions.checkNotNull(member, "member is null");

		MemberArgs<V> memberArgs = new MemberArgs<>();
		memberArgs.setName(name);
		memberArgs.setMember(member);
		return memberArgs;
	}

}
