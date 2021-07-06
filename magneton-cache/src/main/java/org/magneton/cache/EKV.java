package org.magneton.cache;

/**
 * .
 *
 * @author zhangmsh 2021/6/24
 * @since 1.0.0
 */
public class EKV<T> {

  private final long expire;
  private final String key;
  private final T value;

  public EKV(String key, T value, long expire) {
    this.key = key;
    this.value = value;
    this.expire = expire;
  }

  public static <T> EKV of(String key, T value, long expire) {
    return new EKV(key, value, expire);
  }

  public String getKey() {
    return this.key;
  }

  public T getValue() {
    return this.value;
  }

  public long getExpire() {
    return this.expire;
  }
}
