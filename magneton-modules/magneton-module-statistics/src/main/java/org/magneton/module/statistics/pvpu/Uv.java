package org.magneton.module.statistics.pvpu;

import org.magneton.annotation.Cleanup;

/**
 * UV
 *
 * @author zhangmsh.
 * @since M2023.9
 */
public interface Uv {

	/**
	 * 判断是否为独立的访客
	 * @param id UV Id
	 * @return 如果目前属于UV，则返回{@code true}， 否则返回{@code false}
	 */
	boolean is(long id);

	/**
	 * 如果当前为独立访客，则记录为UV
	 * @param id UV Id
	 * @return 如果目前属于UV，并添加成功，则返回{@code true}， 否则返回{@code false}
	 */
	boolean set(long id);

	/**
	 * 删除UV
	 * @param id UV Id
	 * @return 如果对应 {@code id} 已经被记录为UV（即此时 {@link #is(long)} 为
	 * {@code false}），并删除成功，则返回{@code true}， 否则返回{@code false}（即此时 {@link #is(long)} 为
	 * {@code true}）。
	 */
	boolean remove(long id);

	/**
	 * 清除统计数据
	 */
	@Cleanup
	void clean();

}