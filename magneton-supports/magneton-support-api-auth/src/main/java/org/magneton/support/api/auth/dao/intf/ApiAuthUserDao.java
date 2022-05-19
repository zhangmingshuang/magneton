package org.magneton.support.api.auth.dao.intf;

import cn.org.atool.fluent.mybatis.base.IBaseDao;
import com.google.common.base.Preconditions;
import javax.annotation.Nullable;
import org.magneton.support.api.auth.entity.ApiAuthUserDO;
import org.magneton.support.api.auth.wrapper.ApiAuthUserQuery;
import org.magneton.support.constant.Removed;

/**
 * ApiAuthUserDao: 数据操作接口
 *
 * 这只是一个减少手工创建的模板文件 可以任意添加方法和实现, 更改作者和重定义类名
 * <p/>
 *
 * @author Powered By Fluent Mybatis
 */
public interface ApiAuthUserDao extends IBaseDao<ApiAuthUserDO> {

	@Nullable
	default ApiAuthUserDO getByMobile(String mobile) {
		Preconditions.checkNotNull(mobile);
		ApiAuthUserQuery query = new ApiAuthUserQuery().where().removed().eq(Removed.NOT).account().eq(mobile).end();
		return (ApiAuthUserDO) this.mapper().findOne(ApiAuthUserDO.class, query).orElse(null);
	}

}
