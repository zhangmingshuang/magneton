package org.magneton.module.statistics.process;

/**
 * PVUV.
 *
 * @apiNote 1、pv的全称是page
 * view，译为页面浏览量或点击量，通常是衡量一个网站甚至一条网络新闻的指标。用户每次对网站中的一个页面的请求或访问均被记录1个PV，用户对同一页面的多次访问，pv累计。例如，用户访问了4个页面，pv就+4
 * 2、uv的全称是unique view，译为通过互联网访问、浏览这个网页的自然人，访问网站的一台电脑客户端被视为一个访客，在同一天内相同的客户端只被计算一次。
 * @author zhangmsh 18/03/2022
 * @since 2.0.7
 */
public interface PvUv {

	/**
	 * 判断是否为UV
	 * @param group 分组
	 * @param id UV Id
	 * @return 如果是UV，则返回{@code true}
	 */
	default boolean isUv(String group, int id) {
		return this.isUv(group, id, -1);
	}

	/**
	 * 判断是否为UV
	 * @param group 分组
	 * @param id UV Id
	 * @param timeToLiveSeconds 有效欺
	 * @return 如果是UV，则返回{@code true}
	 */
	boolean isUv(String group, int id, long timeToLiveSeconds);

}
