package org.magneton.test.generate;

import java.util.concurrent.ThreadLocalRandom;
import org.magneton.test.generate.base.GenericGenerator;
import org.magneton.test.generate.util.ChineseCharUtils;

public class ChineseAddressGenerator extends GenericGenerator {

  private static GenericGenerator instance = new ChineseAddressGenerator();

  private ChineseAddressGenerator() {}

  public static GenericGenerator getInstance() {
    return instance;
  }

  @Override
  public String generate() {
    StringBuilder result = new StringBuilder(genProvinceAndCity());
    result.append(ChineseCharUtils.genRandomLengthChineseChars(2, 3) + "路");
    result.append(ThreadLocalRandom.current().nextInt(1, 8000) + "号");
    result.append(ChineseCharUtils.genRandomLengthChineseChars(2, 3) + "小区");
    result.append(ThreadLocalRandom.current().nextInt(1, 20) + "单元");
    result.append(ThreadLocalRandom.current().nextInt(101, 2500) + "室");
    return result.toString();
  }

  private static String genProvinceAndCity() {
    return ChineseAreaList.provinceCityList.get(
        ThreadLocalRandom.current().nextInt(0, ChineseAreaList.provinceCityList.size()));
  }
}
