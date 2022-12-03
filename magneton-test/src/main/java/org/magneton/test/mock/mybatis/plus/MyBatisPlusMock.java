package org.magneton.test.mock.mybatis.plus;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * .
 *
 * @author zhangmsh 2022/5/16
 * @since 2.0.8
 */
public class MyBatisPlusMock {

	private static final MyBatisPlusContext MY_BATIS_PLUS_CONTEXT;

	private MyBatisPlusMock() {
	}

	static {
		MY_BATIS_PLUS_CONTEXT = new MyBatisPlusContext();
	}

	public static final <T extends BaseMapper> T mock(Class<T> inf) {
		return (T) new MockBaseMapper(MY_BATIS_PLUS_CONTEXT, inf).getProxy();
	}

	public static MyBatisPlusContext getMyBatisPlusContext() {
		return MY_BATIS_PLUS_CONTEXT;
	}

}
