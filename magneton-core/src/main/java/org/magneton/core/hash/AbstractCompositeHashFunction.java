/*
 * Copyright (C) 2011 The Guava Authors
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
import static org.magneton.core.base.Preconditions.checkNotNull;

/**
 * An abstract composition of multiple hash functions. {@linkplain #newHasher()} delegates
 * to the {@code Hasher} objects of the delegate hash functions, and in the end, they are
 * used by {@linkplain #makeHash(org.magneton.core.hash.Hasher[])} that constructs the
 * final {@code HashCode}.
 *
 * @author Dimitris Andreou
 */
@Immutable
@ElementTypesAreNonnullByDefault
abstract class AbstractCompositeHashFunction extends org.magneton.core.hash.AbstractHashFunction {

	private static final long serialVersionUID = 0L;

	// array not modified after creation
	final org.magneton.core.hash.HashFunction[] functions;

	AbstractCompositeHashFunction(org.magneton.core.hash.HashFunction... functions) {
		for (HashFunction function : functions) {
			checkNotNull(function);
		}
		this.functions = functions;
	}

	/**
	 * Constructs a {@code HashCode} from the {@code Hasher} objects of the functions.
	 * Each of them has consumed the entire input and they are ready to output a
	 * {@code HashCode}. The order of the hashers are the same order as the functions
	 * given to the constructor.
	 */
	// this could be cleaner if it passed HashCode[], but that would create yet another
	// array...
	/* protected */ abstract org.magneton.core.hash.HashCode makeHash(org.magneton.core.hash.Hasher[] hashers);

	@Override
	public org.magneton.core.hash.Hasher newHasher() {
		org.magneton.core.hash.Hasher[] hashers = new org.magneton.core.hash.Hasher[functions.length];
		for (int i = 0; i < hashers.length; i++) {
			hashers[i] = functions[i].newHasher();
		}
		return fromHashers(hashers);
	}

	@Override
	public org.magneton.core.hash.Hasher newHasher(int expectedInputSize) {
		checkArgument(expectedInputSize >= 0);
		org.magneton.core.hash.Hasher[] hashers = new org.magneton.core.hash.Hasher[functions.length];
		for (int i = 0; i < hashers.length; i++) {
			hashers[i] = functions[i].newHasher(expectedInputSize);
		}
		return fromHashers(hashers);
	}

	private org.magneton.core.hash.Hasher fromHashers(org.magneton.core.hash.Hasher[] hashers) {
		return new org.magneton.core.hash.Hasher() {
			@Override
			public org.magneton.core.hash.Hasher putByte(byte b) {
				for (org.magneton.core.hash.Hasher hasher : hashers) {
					hasher.putByte(b);
				}
				return this;
			}

			@Override
			public org.magneton.core.hash.Hasher putBytes(byte[] bytes) {
				for (org.magneton.core.hash.Hasher hasher : hashers) {
					hasher.putBytes(bytes);
				}
				return this;
			}

			@Override
			public org.magneton.core.hash.Hasher putBytes(byte[] bytes, int off, int len) {
				for (org.magneton.core.hash.Hasher hasher : hashers) {
					hasher.putBytes(bytes, off, len);
				}
				return this;
			}

			@Override
			public org.magneton.core.hash.Hasher putBytes(ByteBuffer bytes) {
				int pos = bytes.position();
				for (org.magneton.core.hash.Hasher hasher : hashers) {
					org.magneton.core.hash.Java8Compatibility.position(bytes, pos);
					hasher.putBytes(bytes);
				}
				return this;
			}

			@Override
			public org.magneton.core.hash.Hasher putShort(short s) {
				for (org.magneton.core.hash.Hasher hasher : hashers) {
					hasher.putShort(s);
				}
				return this;
			}

			@Override
			public org.magneton.core.hash.Hasher putInt(int i) {
				for (org.magneton.core.hash.Hasher hasher : hashers) {
					hasher.putInt(i);
				}
				return this;
			}

			@Override
			public org.magneton.core.hash.Hasher putLong(long l) {
				for (org.magneton.core.hash.Hasher hasher : hashers) {
					hasher.putLong(l);
				}
				return this;
			}

			@Override
			public org.magneton.core.hash.Hasher putFloat(float f) {
				for (org.magneton.core.hash.Hasher hasher : hashers) {
					hasher.putFloat(f);
				}
				return this;
			}

			@Override
			public org.magneton.core.hash.Hasher putDouble(double d) {
				for (org.magneton.core.hash.Hasher hasher : hashers) {
					hasher.putDouble(d);
				}
				return this;
			}

			@Override
			public org.magneton.core.hash.Hasher putBoolean(boolean b) {
				for (org.magneton.core.hash.Hasher hasher : hashers) {
					hasher.putBoolean(b);
				}
				return this;
			}

			@Override
			public org.magneton.core.hash.Hasher putChar(char c) {
				for (org.magneton.core.hash.Hasher hasher : hashers) {
					hasher.putChar(c);
				}
				return this;
			}

			@Override
			public org.magneton.core.hash.Hasher putUnencodedChars(CharSequence chars) {
				for (org.magneton.core.hash.Hasher hasher : hashers) {
					hasher.putUnencodedChars(chars);
				}
				return this;
			}

			@Override
			public org.magneton.core.hash.Hasher putString(CharSequence chars, Charset charset) {
				for (org.magneton.core.hash.Hasher hasher : hashers) {
					hasher.putString(chars, charset);
				}
				return this;
			}

			@Override
			public <T> org.magneton.core.hash.Hasher putObject(@ParametricNullness T instance,
					Funnel<? super T> funnel) {
				for (Hasher hasher : hashers) {
					hasher.putObject(instance, funnel);
				}
				return this;
			}

			@Override
			public HashCode hash() {
				return makeHash(hashers);
			}
		};
	}

}
