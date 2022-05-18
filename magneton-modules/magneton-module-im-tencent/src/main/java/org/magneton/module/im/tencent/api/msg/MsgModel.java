package org.magneton.module.im.tencent.api.msg;

public enum MsgModel {

	TO_FROM_ACCOUNT(1), IGNORE_FROM_ACCOUNT(2);

	private final int code;

	MsgModel(int code) {
		this.code = code;
	}

	public int getCode() {
		return this.code;
	}

}