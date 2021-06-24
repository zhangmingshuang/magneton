package org.magneton.cache.util;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * .
 *
 * @author zhangmsh 2021/6/24
 * @since 1.0.0
 */
public class Trans {

  private Trans() {
    // private
  }

  public static String toStr(byte[] response) {
    return new String(response, StandardCharsets.UTF_8);
  }

  public static byte[] toByte(String str) {
    return str.getBytes(StandardCharsets.UTF_8);
  }

  public static byte[][] toBytes(String... strs) {
    byte[][] bytes = new byte[strs.length][];
    for (int i = 0, l = strs.length; i < l; i++) {
      bytes[i] = strs[i].getBytes(StandardCharsets.UTF_8);
    }
    return bytes;
  }

  public static byte[][] toBytes(List<String> strs) {
    byte[][] bytes = new byte[strs.size()][];
    for (int i = 0, l = strs.size(); i < l; i++) {
      bytes[i] = strs.get(i).getBytes(StandardCharsets.UTF_8);
    }
    return bytes;
  }

  public static List<String> toStr(List<byte[]> bytes) {
    List<String> list = new ArrayList<>(bytes.size());
    for (byte[] bs : bytes) {
      list.add(new String(bs, StandardCharsets.UTF_8));
    }
    return list;
  }
}
