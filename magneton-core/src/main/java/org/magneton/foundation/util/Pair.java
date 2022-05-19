package org.magneton.foundation.util;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

/**
 * @author zhangmsh 2021/7/17
 * @since 1.0.0
 */
public final class Pair<S, T> {

	private final S first;

	private final T second;

	private Pair(S first, T second) {

		Preconditions.checkNotNull(first, "First must not be null!");
		Preconditions.checkNotNull(second, "Second must not be null!");

		this.first = first;
		this.second = second;
	}

	/**
	 * Creates a new {@link Pair} for the given elements.
	 * @param first must not be {@literal null}.
	 * @param second must not be {@literal null}.
	 * @return the pair
	 */
	public static <S, T> Pair<S, T> of(S first, T second) {
		return new Pair<>(first, second);
	}

	/**
	 * Returns the first element of the {@link Pair}.
	 * @return
	 */
	public S getFirst() {
		return this.first;
	}

	public S getKey() {
		return this.first;
	}

	/**
	 * Returns the second element of the {@link Pair}.
	 * @return
	 */
	public T getSecond() {
		return this.second;
	}

	public T getValue() {
		return this.second;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Strings.lenientFormat("%s->%s", this.first, this.second);
	}

}
