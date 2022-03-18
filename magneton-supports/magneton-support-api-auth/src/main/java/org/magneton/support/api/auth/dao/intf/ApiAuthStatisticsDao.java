package org.magneton.support.api.auth.dao.intf;

import javax.annotation.Nullable;

import cn.org.atool.fluent.mybatis.base.IBaseDao;
import org.magneton.core.base.Preconditions;
import org.magneton.support.api.auth.entity.ApiAuthStatisticsDO;
import org.magneton.support.api.auth.helper.ApiAuthStatisticsSegment;
import org.magneton.support.api.auth.wrapper.ApiAuthStatisticsQuery;
import org.magneton.support.api.auth.wrapper.ApiAuthStatisticsUpdate;

/**
 * ApiAuthStatisticsDao: 数据操作接口
 *
 * 这只是一个减少手工创建的模板文件 可以任意添加方法和实现, 更改作者和重定义类名
 * <p/>
 *
 * @author Powered By Fluent Mybatis
 */
public interface ApiAuthStatisticsDao extends IBaseDao<ApiAuthStatisticsDO> {

	@Nullable
	default ApiAuthStatisticsDO getByToday(String today) {
		Preconditions.checkNotNull(today);
		ApiAuthStatisticsQuery query = new ApiAuthStatisticsQuery().where().today().eq(today).end();
		return (ApiAuthStatisticsDO) this.mapper().findOne(ApiAuthStatisticsDO.class, query).orElse(null);
	}

	default void increPvUv(String today, boolean isUv) {
		Preconditions.checkNotNull(today);
		ApiAuthStatisticsSegment.UpdateSetter setter = new ApiAuthStatisticsUpdate().set.pv().applyFunc("pv = pv + ?",
				1);
		if (isUv) {
			setter.uv().applyFunc("uv = uv + ?", 1);
		}
		ApiAuthStatisticsUpdate update = setter.end().where().today().eq(today).end();
		this.mapper().updateBy(update);
	}

}
