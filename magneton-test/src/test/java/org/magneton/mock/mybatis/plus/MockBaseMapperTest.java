package org.magneton.mock.mybatis.plus;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.junit.jupiter.api.Test;
import org.magneton.test.mock.mybatis.plus.MockBaseMapper;
import org.magneton.test.mock.mybatis.plus.MyBatisPlusMock;

/**
 * .
 *
 * @author zhangmsh 2022/5/16
 * @since 2.0.8
 */
class MockBaseMapperTest {

	@Test
	void test() {
		MockBaseMapper<TestBaseMapper> mock = new MockBaseMapper<>(MyBatisPlusMock.getMyBatisPlusContext(),
				TestBaseMapper.class);
		TestBaseMapper proxy = mock.getProxy();
	}

	private interface TestBaseMapper extends BaseMapper {

	}

}