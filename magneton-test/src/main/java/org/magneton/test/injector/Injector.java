package org.magneton.test.injector;

import javax.annotation.Nullable;

import org.magneton.test.config.Config;
import org.magneton.test.core.InjectType;
import org.magneton.test.parser.Definition;

/**
 * 注射器.
 *
 * @author zhangmsh 2021/8/18
 * @since 2.0.0
 */
public interface Injector {

	/**
	 * 空类列表
	 */
	Class[] EMPTY_CLASSES = new Class[0];

	/**
	 * 类型列表
	 * @return 类型列表
	 */
	Class[] getTypes();

	/**
	 * 表示该注射器排在某个注射类型之后
	 *
	 * <p>
	 * 也就是如，如果在该注射器优先被选择到，则会根据该之后的配置进行判断。
	 *
	 * <p>
	 * 如果有配置，则优先采用这个配置的注射器，如果这个配置的注射器不处理，才使用该注射器进行处理
	 * @return 注射器类型列表
	 */
	default Class[] afterTypes() {
		return EMPTY_CLASSES;
	}

	@Nullable
	<T> T inject(Definition definition, Config config, InjectType injectType);

}
