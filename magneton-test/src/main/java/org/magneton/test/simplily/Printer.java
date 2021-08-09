package org.magneton.test.simplily;

import com.google.common.base.Strings;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import javax.annotation.Nullable;

/**
 * @author zhangmsh 2021/8/9
 * @since 1.0.0
 */
public class Printer {
  private static final Printer PRINTER = new Printer();

  public static Printer getInstance() {
    return PRINTER;
  }

  @CanIgnoreReturnValue
  public String print(@Nullable Object obj) {
    return this.print("", obj);
  }

  @CanIgnoreReturnValue
  public String print(String prefix, @Nullable Object obj) {
    String string = StringSimplily.getInstance().string(obj);
    if (!Strings.isNullOrEmpty(string)) {
      System.out.println("---");
      System.out.println(prefix + string);
      System.out.println();
    }
    return string;
  }
}
