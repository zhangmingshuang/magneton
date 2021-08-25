package org.magneton.test.core;

import lombok.Getter;

/**
 * @author zhangmsh 2021/8/9
 * @since 1.0.0
 */
@Getter
public class TraceChain {
  private static final ThreadLocal<TraceChain> THREAD_LOCAL = new ThreadLocal();

  private Class root;

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
}
