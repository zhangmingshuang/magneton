/*
 * Copyright (C) 2017 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.magneton.core.hash;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import javax.annotation.concurrent.Immutable;

import static org.magneton.core.base.Preconditions.checkArgument;
import static org.magneton.core.base.Preconditions.checkPositionIndexes;

/**
 * Skeleton implementation of {@link org.magneton.core.hash.HashFunction} in terms of
 * {@link #newHasher()}.
 *
 * <p>
 * TODO(lowasser): make public
 */
@Immutable
@ElementTypesAreNonnullByDefault
abstract class AbstractHashFunction implements HashFunction {

	@Override
	public <T> org.magneton.core.hash.HashCode hashObject(@ParametricNullness T instance, Funnel<? super T> funnel) {
		return newHasher().putObject(instance, funnel).hash();
	}

	@Override
	public org.magneton.core.hash.HashCode hashUnencodedChars(CharSequence input) {
		int len = input.length();
		return newHasher(len * 2).putUnencodedChars(input).hash();
	}

	@Override
	public org.magneton.core.hash.HashCode hashString(CharSequence input, Charset charset) {
		return newHasher().putString(input, charset).hash();
	}

	@Override
	public org.magneton.core.hash.HashCode hashInt(int input) {
		return newHasher(4).putInt(input).hash();
	}

	@Override
	public org.magneton.core.hash.HashCode hashLong(long input) {
		return newHasher(8).putLong(input).hash();
	}

	@Override
	public org.magneton.core.hash.HashCode hashBytes(byte[] input) {
		return hashBytes(input, 0, input.length);
	}

	@Override
	public org.magneton.core.hash.HashCode hashBytes(byte[] input, int off, int len) {
		checkPositionIndexes(off, off + len, input.length);
		return newHasher(len).putBytes(input, off, len).hash();
	}

	@Override
	public HashCode hashBytes(ByteBuffer input) {
		return newHasher(input.remaining()).putBytes(input).hash();
	}

	@Override
	public Hasher newHasher(int expectedInputSize) {
		checkArgument(expectedInputSize >= 0, "expectedInputSize must be >= 0 but was %s", expectedInputSize);
		return newHasher();
	}

}
