package org.magneton.test.model;

import javax.annotation.Nullable;

import org.magneton.test.model.generate.EmailAddressGeneratorAbstract;
import org.magneton.test.model.generate.base.AbstractGenericGenerator;

public enum StringModel {

	/**
	 * 正常模式
	 */
	NORMAL(null),
	/**
	 * 邮箱模式
	 */
	EMAIL(EmailAddressGeneratorAbstract.getInstance());

	private final AbstractGenericGenerator abstractGenericGenerator;

	StringModel(AbstractGenericGenerator abstractGenericGenerator) {
		this.abstractGenericGenerator = abstractGenericGenerator;
	}

	@Nullable
	public AbstractGenericGenerator getGenericGenerator() {
		return this.abstractGenericGenerator;
	}

}
