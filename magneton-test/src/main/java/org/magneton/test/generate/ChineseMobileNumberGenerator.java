package org.magneton.test.generate;

import java.util.concurrent.ThreadLocalRandom;
import org.magneton.test.generate.base.GenericGenerator;
import org.magneton.test.util.StringUtil;

public class ChineseMobileNumberGenerator extends GenericGenerator {

  private static final int[] MOBILE_PREFIX = {
    133, 153, 177, 180, 181, 189, 134, 135, 136, 137, 138, 139, 150, 151, 152, 157, 158, 159, 178,
    182, 183, 184, 187, 188, 130, 131, 132, 155, 156, 176, 185, 186, 145, 147, 170
  };

  private static ChineseMobileNumberGenerator instance = new ChineseMobileNumberGenerator();

  private ChineseMobileNumberGenerator() {}

  public static ChineseMobileNumberGenerator getInstance() {
    return instance;
  }

  @Override
  public String generate() {
    return genMobilePre()
        + StringUtil.leftPad("" + ThreadLocalRandom.current().nextInt(0, 99999999 + 1), 8, "0");
  }

  /** 生成假的手机号，以19开头 */
  public String generateFake() {
    return "19"
        + StringUtil.leftPad("" + ThreadLocalRandom.current().nextInt(0, 999999999 + 1), 9, "0");
  }

  private static String genMobilePre() {
    return "" + MOBILE_PREFIX[ThreadLocalRandom.current().nextInt(0, MOBILE_PREFIX.length)];
  }
}
