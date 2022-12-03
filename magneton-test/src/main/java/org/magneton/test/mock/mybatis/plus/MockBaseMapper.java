package org.magneton.test.mock.mybatis.plus;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Set;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.google.common.collect.Sets;
import com.google.common.reflect.Reflection;

/**
 * .
 *
 * @author zhangmsh 2022/5/16
 * @since 2.0.8
 */
public class MockBaseMapper<T extends BaseMapper> implements InvocationHandler {

	private static final Set<Method> BASE_MAPPER_METHODS = Sets.newHashSet();

	static {
		Method[] declaredMethods = BaseMapper.class.getDeclaredMethods();
		for (Method method : declaredMethods) {
			if (method.getDeclaringClass() == Object.class) {
				continue;
			}
			BASE_MAPPER_METHODS.add(method);
		}
	}
	private final BaseMapper baseMapper = new InmemoryBaseMapper();

	private final MyBatisPlusContext myBatisPlusContext;

	private final Class<T> target;

	private final T proxy;

	public MockBaseMapper(MyBatisPlusContext myBatisPlusContext, Class<T> target) {
		this.myBatisPlusContext = myBatisPlusContext;
		this.target = target;
		this.proxy = (T) Reflection.newProxy(target, this);
	}

	public T getProxy() {
		return this.proxy;
	}

	public Class<T> getTarget() {
		return this.target;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if (BASE_MAPPER_METHODS.contains(method)) {
			return method.invoke(this.baseMapper, args);
		}
		// 自定义的方法，需要解析XML关联
		return null;
	}

}
