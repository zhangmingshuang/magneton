package org.magneton.test.model.generate.bank;

import org.magneton.test.model.generate.util.LuhnUtils;
import org.magneton.test.util.StringUtil;

/**
 *
 *
 * <pre>
 * 银行卡号校验类
 * Created by Binary Wang on 2018/3/22.
 * </pre>
 *
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
public class BankCardNumberValidator {

  /**
   * 校验银行卡号是否合法
   *
   * @param cardNo 银行卡号
   * @return 是否合法
   */
  public static boolean validate(String cardNo) {
    if (!StringUtil.isNumeric(cardNo)) {
      return false;
    }

    if (cardNo.length() > 19 || cardNo.length() < 16) {
      return false;
    }

    int luhnSum =
        LuhnUtils.getLuhnSum(cardNo.substring(0, cardNo.length() - 1).trim().toCharArray());
    char checkCode = (luhnSum % 10 == 0) ? '0' : (char) ((10 - luhnSum % 10) + '0');
    return cardNo.substring(cardNo.length() - 1).charAt(0) == checkCode;
  }
}
