package org.magneton.test.core;

import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zhangmsh 2021/8/9
 * @since 1.0.0
 */
@Getter
public class TraceChain {

  private static final ThreadLocal<TraceChain> THREAD_LOCAL = new ThreadLocal();

  private Class root;

  private final Map<Class, Queue<Inject>> injectChain = Maps.newHashMap();

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

  public Class getRoot() {
    return this.root;
  }

  public void inject(Class clazz, @Nullable Object remark) {
    Preconditions.checkNotNull(clazz, "clazz");

    this.injectChain
        .computeIfAbsent(clazz, c -> Queues.newArrayDeque())
        .add(new Inject(clazz, remark));
  }

  public void injectTo(Class clazz, @Nullable Object remark, Class toClazz) {
    Preconditions.checkNotNull(clazz, "clazz");
    Preconditions.checkNotNull(toClazz, "toClazz");

    this.injectChain
        .computeIfAbsent(toClazz, c -> Queues.newArrayDeque())
        .add(new Inject(clazz, remark));
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder(256);
    builder.append("源").append(this.root).append("注入链：");
    Set<Map.Entry<Class, Queue<Inject>>> entries = this.injectChain.entrySet();
    for (Map.Entry<Class, Queue<Inject>> entry : entries) {
      builder.append(entry.getKey()).append("[");
      Queue<Inject> value = entry.getValue();
      Inject inject;
      while ((inject = value.poll()) != null) {
        builder.append(inject.clazz);
        if (Objects.nonNull(inject.remark)) {
          builder.append("(").append(inject.remark).append(")");
        }
        builder.append("-->");
      }
      builder.append("]==>");
    }
    return builder.toString();
  }

  @AllArgsConstructor
  public static class Inject {

    private Class clazz;

    @Nullable private Object remark;
  }
}
