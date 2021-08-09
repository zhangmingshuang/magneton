package org.magneton.test.jsr303.processor;

import java.lang.annotation.Annotation;
import javax.validation.constraints.Email;
import org.magneton.test.annotation.TestComponent;
import org.magneton.test.core.Config;
import org.magneton.test.generate.EmailAddressGenerator;
import org.magneton.test.injector.Inject;
import org.magneton.test.injector.InjectType;
import org.magneton.test.injector.processor.AnnotationProcessor;
import org.magneton.test.injector.processor.statement.DataStatement;

/**
 * {@code @Email(regexp=正则表达式,flag=标志的模式)}
 *
 * <p>验证注解的元素值是 {@code Email}，也可以通过 regexp 和 flag 指定自定义的 email 格式.
 *
 * <p>该处理器只会生成一个标准的Email数据，如果是特殊的Email数据，需要自己定义处理器
 *
 * @author zhangmsh 2021/8/4
 * @since 2.0.0
 */
@TestComponent
public class Jsr303EmailProcessor implements AnnotationProcessor {

  @Override
  public boolean processable(Class annotationType) {
    return Email.class == annotationType;
  }

  @Override
  public void process(
      Config config,
      InjectType injectType,
      Inject inject,
      Annotation annotation,
      DataStatement dataStatement) {
    if (injectType.isDemon()) {
      dataStatement.breakNext(null);
      return;
    }
    String email = EmailAddressGenerator.getInstance().generate();
    dataStatement.setValue(email);
  }
}
