package org.magneton.test.validate;

import java.lang.annotation.Annotation;
import java.util.Map;
import javax.annotation.Nullable;
import org.magneton.test.config.Config;
import org.magneton.test.config.ConfigPostProcessor;
import org.magneton.test.parser.Definition;

/**
 * .
 *
 * @author zhangmsh 2021/8/23
 * @since 2.0.0
 */
public abstract class AbstractConfigPostProcessor implements ConfigPostProcessor {

  @Override
  public void beforeConfig(Config config, Definition definition) {
    Map<Class, Annotation> annotations = definition.getAnnotations();
    if (annotations == null || annotations.isEmpty()) {
      return;
    }
    Class[] classes = this.jsrAnnotations();
    if (classes == null || classes.length < 1) {
      return;
    }
    for (Class clazz : classes) {
      if (annotations.containsKey(clazz)) {
        this.doPostProcessor(annotations.get(clazz), config, definition);
      }
    }
  }

  protected abstract void doPostProcessor(
      Annotation annotation, Config config, Definition definition);

  @Nullable
  protected abstract Class[] jsrAnnotations();
}
