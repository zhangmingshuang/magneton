/*
 * Copyright (C) 2012 The Guava Authors
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
import java.nio.ByteOrder;

import javax.annotations.CanIgnoreReturnValue;

import org.magneton.core.primitives.Chars;
import org.magneton.core.primitives.Ints;
import org.magneton.core.primitives.Longs;
import org.magneton.core.primitives.Shorts;

import static org.magneton.core.base.Preconditions.checkNotNull;
import static org.magneton.core.base.Preconditions.checkPositionIndexes;

/**
 * Abstract {@link org.magneton.core.hash.Hasher} that handles converting primitives to
 * bytes using a scratch {@code
 * ByteBuffer} and streams all bytes to a sink to compute the hash.
 *
 * @author Colin Decker
 */
@CanIgnoreReturnValue
@ElementTypesAreNonnullByDefault
abstract class AbstractByteHasher extends org.magneton.core.hash.AbstractHasher {

	private final ByteBuffer scratch = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);

	/** Updates this hasher with the given byte. */
	protected abstract void update(byte b);

	/** Updates this hasher with the given bytes. */
	protected void update(byte[] b) {
		update(b, 0, b.length);
	}

	/**
	 * Updates this hasher with {@code len} bytes starting at {@code off} in the given
	 * buffer.
	 */
	protected void update(byte[] b, int off, int len) {
		for (int i = off; i < off + len; i++) {
			update(b[i]);
		}
	}

	/** Updates this hasher with bytes from the given buffer. */
	protected void update(ByteBuffer b) {
		if (b.hasArray()) {
			update(b.array(), b.arrayOffset() + b.position(), b.remaining());
			org.magneton.core.hash.Java8Compatibility.position(b, b.limit());
		}
		else {
			for (int remaining = b.remaining(); remaining > 0; remaining--) {
				update(b.get());
			}
		}
	}

	/** Updates the sink with the given number of bytes from the buffer. */
	private org.magneton.core.hash.Hasher update(int bytes) {
		try {
			update(scratch.array(), 0, bytes);
		}
		finally {
			org.magneton.core.hash.Java8Compatibility.clear(scratch);
		}
		return this;
	}

	@Override
	public org.magneton.core.hash.Hasher putByte(byte b) {
		update(b);
		return this;
	}

	@Override
	public org.magneton.core.hash.Hasher putBytes(byte[] bytes) {
		checkNotNull(bytes);
		update(bytes);
		return this;
	}

	@Override
	public org.magneton.core.hash.Hasher putBytes(byte[] bytes, int off, int len) {
		checkPositionIndexes(off, off + len, bytes.length);
		update(bytes, off, len);
		return this;
	}

	@Override
	public org.magneton.core.hash.Hasher putBytes(ByteBuffer bytes) {
		update(bytes);
		return this;
	}

	@Override
	public org.magneton.core.hash.Hasher putShort(short s) {
		scratch.putShort(s);
		return update(Shorts.BYTES);
	}

	@Override
	public org.magneton.core.hash.Hasher putInt(int i) {
		scratch.putInt(i);
		return update(Ints.BYTES);
	}

	@Override
	public org.magneton.core.hash.Hasher putLong(long l) {
		scratch.putLong(l);
		return update(Longs.BYTES);
	}

	@Override
	public Hasher putChar(char c) {
		scratch.putChar(c);
		return update(Chars.BYTES);
	}

}
