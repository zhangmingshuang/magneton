package org.magneton.test.test.validate;

import org.magneton.test.test.annotation.TestComponent;
import org.magneton.test.test.config.Config;
import org.magneton.test.test.model.StringModel;
import org.magneton.test.test.parser.Definition;
import java.lang.annotation.Annotation;
import javax.annotation.Nullable;
import javax.validation.constraints.Email;

/**
 * {@code @Email(regexp=正则表达式,flag=标志的模式)}
 *
 * <p>
 * 验证注解的元素值是 {@code Email}，也可以通过 regexp 和 flag 指定自定义的 email 格式.
 *
 * <p>
 * 该处理器只会生成一个标准的Email数据，如果是特殊的Email数据，需要自己定义处理器
 *
 * @author zhangmsh 2021/8/23
 * @since 2.0.0
 */
@TestComponent
public class EmailConfigPostProcessor extends AbstractConfigPostProcessor {

	@Override
	protected void doPostProcessor(Annotation annotation, Config config, Definition definition) {
		config.setStringMode(StringModel.EMAIL);
	}

	@Nullable
	@Override
	protected Class[] jsrAnnotations() {
		return new Class[] { Email.class };
	}

}