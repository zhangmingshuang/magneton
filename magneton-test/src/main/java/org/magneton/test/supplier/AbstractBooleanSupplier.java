package org.magneton.test.supplier;

import java.util.function.BooleanSupplier;

import javax.annotation.Nullable;

/**
 * .
 *
 * @author zhangmsh 2021/8/6
 * @since 2.0.0
 */
public abstract class AbstractBooleanSupplier<T> extends AbstractSupplier<T> implements BooleanSupplier {

	protected AbstractBooleanSupplier(@Nullable Object obj) {
		super(obj);
	}

	@Override
	public boolean getAsBoolean() {
		try {
			return this.doBooleanJudge();
		}
		finally {
			this.takeErrors();
		}
	}

	public BooleanSupplier sout() {
		this.setPrint(true);
		return this;
	}

	protected abstract boolean doBooleanJudge();

}
