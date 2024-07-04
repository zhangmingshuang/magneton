package org.magneton.enhance.pay.core;

/**
 * .
 *
 * @author zhangmsh 30/03/2022
 * @since 2.0.7
 */
public interface TradeType {

	int getCode();

	String getDescription();

	boolean isName(String name);

}
