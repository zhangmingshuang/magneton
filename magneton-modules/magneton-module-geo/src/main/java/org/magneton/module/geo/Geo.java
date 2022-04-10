package org.magneton.module.geo;

import java.util.Map;
import javax.annotation.Nullable;
import org.magneton.module.geo.query.MemberArgs;
import org.magneton.module.geo.query.PositionArgs;

/**
 * .
 *
 * @author zhangmsh 2022/2/11
 * @since 1.2.0
 */
@SuppressWarnings("unchecked")
public interface Geo {

	/**
	 * 添加一个位置
	 * @param name 名称
	 * @param longitude 经度，必须在 -180 到 180 范围内
	 * @param latitude 纬度，必须在 -85.05112878 到 85.05112878 范围内
	 * @param member 要添加的成员元素
	 * @return 成功添加的数量，如果要添加的 {@code name}已经存在，则不计数。
	 */
	default <V> long add(String name, double longitude, double latitude, V member) {
		return this.add(name, GeoEntry.of(longitude, latitude, member));
	}

	/**
	 * 添加一个位置
	 * @param name 名称
	 * @param entries 要添加的经纬度数据
	 * @return 成功添加的数量，如果要添加的 {@code name}已经存在，则不计数。
	 */
	<V> long add(String name, GeoEntry<V>... entries);

	/**
	 * 如果位置不存在，则添加。如果已存在则忽略。
	 * @param name 名称
	 * @param longitude 经度，必须在 -180 到 180 范围内
	 * @param latitude 纬度，必须在 -85.05112878 到 85.05112878 范围内
	 * @param member 要添加的成员元素
	 * @return 成功添加的数量
	 */
	default <V> long addIfAbsent(String name, double longitude, double latitude, V member) {
		return this.addIfAbsent(name, GeoEntry.of(longitude, latitude, member));
	}

	/**
	 * 如果位置不存在，则添加。如果已存在则忽略。
	 * @param name 名称
	 * @param entries 要添加的经纬度数据
	 * @return 成功添加的数量
	 */
	<V> long addIfAbsent(String name, GeoEntry<V>... entries);

	/**
	 * 如果位置已经存在，则更新，如果不存在则忽略。
	 * @param name 名称
	 * @param longitude 经度，必须在 -180 到 180 范围内
	 * @param latitude 纬度，必须在 -85.05112878 到 85.05112878 范围内
	 * @param member 要添加的成员元素
	 * @return 成功添加的数量
	 */
	default <V> long addIfExists(String name, double longitude, double latitude, V member) {
		return this.addIfExists(name, GeoEntry.of(longitude, latitude, member));
	}

	/**
	 * 如果位置已经存在，则更新，如果不存在则忽略。
	 * @param name 名称
	 * @param entries 要添加的经纬度数据
	 * @return 成功添加的数量
	 */
	<V> long addIfExists(String name, GeoEntry<V>... entries);

	/**
	 * 计算两个成员元素之间的距离
	 * @param name 名称
	 * @param member1 成员元素1
	 * @param member2 成员元素2
	 * @return 两个成员元素之间的距离，单位：米。如果两个成员有任意其中一个成员不存在，则返回 {@code null}
	 */
	@Nullable
	default <V> Double dist(String name, V member1, V member2) {
		return this.dist(name, member1, member2, GeoUnit.METERS);
	}

	/**
	 * 计算两个成员元素之间的距离
	 * @param name 名称
	 * @param member1 成员元素1
	 * @param member2 成员元素2
	 * @param unit 返回的距离单位
	 * @return 两个成员元素之间的距离。如果两个成员有任意其中一个成员不存在，则返回 {@code null}
	 */
	<V> Double dist(String name, V member1, V member2, GeoUnit unit);

	/**
	 * 获取成员元素的经纬度信息
	 * @param name 名称
	 * @param members 要查询经纬度的成员元素的列表，如果不传则直接返回空列表
	 * @return 成员元素列中对应的经纬度信息，如果成员不存在，则对应的经纬度信息为 {@code null}
	 */
	<V> Map<V, GeoPosition> pos(String name, V... members);

	// ---------------------------DISTANCE-------------------------------

	/**
	 * 获取某个成员元素的半径成员元素数据
	 * @param args 查询条件
	 * @return 对应成员元素半径的所有成员元素数据及对应的距离（单位与查找半径单位一致）列表。
	 */
	<V> Map<V, Double> radiusWithDistance(MemberArgs<V> args);

	/**
	 * 获取对应经纬度位置的半径成员元素数据
	 * @param args 查询条件
	 * @return 对应经纬度半径的所有成员元素数据及对应的距离（单位与查找半径单位一致）列表。
	 */
	<V> Map<V, Double> radiusWithDistance(PositionArgs args);

	// ------------------------------POSITION----------------------------------------

	/**
	 * 获取某个成员元素的半径成员元素的经纬度信息
	 * @param args 查询参数
	 * @return 对应成员元素半径的所有成员元素对应的经纬度信息数据。
	 */
	<V> Map<V, GeoPosition> radiusWithPosition(MemberArgs<V> args);

	/**
	 * 获取对应经纬度位置的半径成员元素的经纬度信息
	 * @param args 查询参数
	 * @return 对应经纬度半径的所有成员元素的经纬度信息
	 */
	<V> Map<V, GeoPosition> radiusWithPosition(PositionArgs args);

}
