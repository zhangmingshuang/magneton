package org.magneton.spy.core;

import org.magneton.spy.constant.DB;
import org.magneton.spy.sql.Select;

/**
 * @author zhangmsh 2021/9/22
 * @since 1.0.0
 */
public interface Database {

	DB db();

	Select select();

}
