package org.magneton.enhance.im.tencent.api.msg;

public enum MsgModel {

	/**
	 * 把消息同步到 From_Account 在线终端和漫游上
	 */
	TO_FROM_ACCOUNT(1),
	/**
	 * 消息不同步至 From_Account；若不填写默认情况下会将消息存 From_Account 漫游
	 */
	IGNORE_FROM_ACCOUNT(2);

	private final int code;

	MsgModel(int code) {
		this.code = code;
	}

	public int getCode() {
		return this.code;
	}

}