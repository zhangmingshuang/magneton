//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.magneton.foundiation;

import cn.hutool.core.math.BitStatusUtil;
import lombok.Getter;

public class BitStatus {

	@Getter
	private int states = 0;

	public int add(int stat) {
		this.states = BitStatusUtil.add(this.states, stat);
		return this.states;
	}

	public boolean has(int stat) {
		return BitStatusUtil.has(this.states, stat);
	}

	public int remove(int stat) {
		return BitStatusUtil.remove(this.states, stat);
	}

	public int clear() {
		this.states = 0;
		return this.states;
	}

}
