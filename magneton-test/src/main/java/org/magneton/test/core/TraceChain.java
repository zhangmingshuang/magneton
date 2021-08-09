package org.magneton.test.core;

import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nullable;
import lombok.Getter;
import org.magneton.test.injector.Injector;

/**
 * @author zhangmsh 2021/8/9
 * @since 1.0.0
 */
@Getter
public class TraceChain {
  private static final ThreadLocal<TraceChain> THREAD_LOCAL = new ThreadLocal();

  private Class root;

  private List<Injector> injectorChain = Lists.newArrayList();
  private Injector currentInjector;

  private TraceChain() {}

  public static TraceChain current() {
    TraceChain traceChain = THREAD_LOCAL.get();
    if (traceChain == null) {
      traceChain = new TraceChain();
      THREAD_LOCAL.set(traceChain);
    }
    return traceChain;
  }

  public void start(Class clazz) {
    this.root = clazz;
  }

  public void end() {
    THREAD_LOCAL.remove();
  }

  public void injector(@Nullable Injector injector) {
    this.currentInjector = injector;
    if (injector != null) {
      this.injectorChain.add(injector);
    }
  }
}
