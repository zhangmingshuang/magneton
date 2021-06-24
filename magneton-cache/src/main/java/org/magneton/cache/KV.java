package org.magneton.cache;

/**
 * .
 *
 * @author zhangmsh 2021/6/24
 * @since 1.0.0
 */
public class KV {
  private final String key;
  private final String value;

  public KV(String key, String value) {
    this.key = key;
    this.value = value;
  }

  public static KV of(String key, String value) {
    return new KV(key, value);
  }

  public String getKey() {
    return this.key;
  }

  public String getValue() {
    return this.value;
  }
}
