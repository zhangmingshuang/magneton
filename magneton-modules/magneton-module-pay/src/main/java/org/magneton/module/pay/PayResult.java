package org.magneton.module.pay;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class PayResult {

	private boolean success;

	private String info;

	private int userId;

	private String orderId;

	private int businessId;

}