package org.magneton.foundation.str;

import org.magneton.foundation.BitStatusOperator;

/**
 * 通过位运算表示状态的工具类.
 *
 * @author zhangmsh.
 * @since 2024
 */
public class BitStatus {

	private int status = 0;

	public BitStatus() {
	}

	public BitStatus(int status) {
		this.status = status;
	}

	public void add(int status) {
		this.status = BitStatusOperator.add(this.status, status);
	}

	public void remove(int status) {
		this.status = BitStatusOperator.remove(this.status, status);
	}

	public boolean has(int status) {
		return BitStatusOperator.has(this.status, status);
	}

	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void reset() {
		this.status = 0;
	}

}