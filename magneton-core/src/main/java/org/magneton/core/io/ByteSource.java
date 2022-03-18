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

package org.magneton.core.io;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import javax.annotations.CanIgnoreReturnValue;

import org.magneton.core.base.Ascii;
import org.magneton.core.base.Optional;
import org.magneton.core.base.Preconditions;
import org.magneton.core.collect.ImmutableList;
import org.magneton.core.hash.Funnels;
import org.magneton.core.hash.HashCode;
import org.magneton.core.hash.HashFunction;
import org.magneton.core.hash.Hasher;

/**
 * A readable source of bytes, such as a file. Unlike an {@link InputStream}, a
 * {@code ByteSource} is not an open, stateful stream for input that can be read and
 * closed. Instead, it is an immutable <i>supplier</i> of {@code InputStream} instances.
 *
 * <p>
 * {@code ByteSource} provides two kinds of methods:
 *
 * <ul>
 * <li><b>Methods that return a stream:</b> These methods should return a <i>new</i>,
 * independent instance each time they are called. The caller is responsible for ensuring
 * that the returned stream is closed.
 * <li><b>Convenience methods:</b> These are implementations of common operations that are
 * typically implemented by opening a stream using one of the methods in the first
 * category, doing something and finally closing the stream that was opened.
 * </ul>
 *
 * <p>
 * <b>Note:</b> In general, {@code ByteSource} is intended to be used for "file-like"
 * sources that provide streams that are:
 *
 * <ul>
 * <li><b>Finite:</b> Many operations, such as {@link #size()} and {@link #read()}, will
 * either block indefinitely or fail if the source creates an infinite stream.
 * <li><b>Non-destructive:</b> A <i>destructive</i> stream will consume or otherwise alter
 * the bytes of the source as they are read from it. A source that provides such streams
 * will not be reusable, and operations that read from the stream (including
 * {@link #size()}, in some implementations) will prevent further operations from
 * completing as expected.
 * </ul>
 *
 * @since 14.0
 * @author Colin Decker
 */

@ElementTypesAreNonnullByDefault
public abstract class ByteSource {

	/** Constructor for use by subclasses. */
	protected ByteSource() {
	}

	/**
	 * Concatenates multiple {@link ByteSource} instances into a single source. Streams
	 * returned from the source will contain the concatenated data from the streams of the
	 * underlying sources.
	 *
	 * <p>
	 * Only one underlying stream will be open at a time. Closing the concatenated stream
	 * will close the open underlying stream.
	 * @param sources the sources to concatenate
	 * @return a {@code ByteSource} containing the concatenated data
	 * @since 15.0
	 */
	public static ByteSource concat(Iterable<? extends ByteSource> sources) {
		return new ConcatenatedByteSource(sources);
	}

	/**
	 * Concatenates multiple {@link ByteSource} instances into a single source. Streams
	 * returned from the source will contain the concatenated data from the streams of the
	 * underlying sources.
	 *
	 * <p>
	 * Only one underlying stream will be open at a time. Closing the concatenated stream
	 * will close the open underlying stream.
	 *
	 * <p>
	 * Note: The input {@code Iterator} will be copied to an {@code ImmutableList} when
	 * this method is called. This will fail if the iterator is infinite and may cause
	 * problems if the iterator eagerly fetches data for each source when iterated (rather
	 * than producing sources that only load data through their streams). Prefer using the
	 * {@link #concat(Iterable)} overload if possible.
	 * @param sources the sources to concatenate
	 * @return a {@code ByteSource} containing the concatenated data
	 * @throws NullPointerException if any of {@code sources} is {@code null}
	 * @since 15.0
	 */
	public static ByteSource concat(Iterator<? extends ByteSource> sources) {
		return concat(ImmutableList.copyOf(sources));
	}

	/**
	 * Concatenates multiple {@link ByteSource} instances into a single source. Streams
	 * returned from the source will contain the concatenated data from the streams of the
	 * underlying sources.
	 *
	 * <p>
	 * Only one underlying stream will be open at a time. Closing the concatenated stream
	 * will close the open underlying stream.
	 * @param sources the sources to concatenate
	 * @return a {@code ByteSource} containing the concatenated data
	 * @throws NullPointerException if any of {@code sources} is {@code null}
	 * @since 15.0
	 */
	public static ByteSource concat(ByteSource... sources) {
		return concat(ImmutableList.copyOf(sources));
	}

	/**
	 * Returns a view of the given byte array as a {@link ByteSource}. To view only a
	 * specific range in the array, use {@code ByteSource.wrap(b).slice(offset, length)}.
	 *
	 * <p>
	 * Note that the given byte array may be passed directly to methods on, for example,
	 * {@code
	 * OutputStream} (when {@code copyTo(OutputStream)} is called on the resulting {@code
	 * ByteSource}). This could allow a malicious {@code OutputStream} implementation to
	 * modify the contents of the array, but provides better performance in the normal
	 * case.
	 *
	 * @since 15.0 (since 14.0 as {@code ByteStreams.asByteSource(byte[])}).
	 */
	public static ByteSource wrap(byte[] b) {
		return new ByteArrayByteSource(b);
	}

	/**
	 * Returns an immutable {@link ByteSource} that contains no bytes.
	 *
	 * @since 15.0
	 */
	public static ByteSource empty() {
		return EmptyByteSource.INSTANCE;
	}

	/**
	 * Returns a {@link CharSource} view of this byte source that decodes bytes read from
	 * this source as characters using the given {@link Charset}.
	 *
	 * <p>
	 * If {@link CharSource#asByteSource} is called on the returned source with the same
	 * charset, the default implementation of this method will ensure that the original
	 * {@code ByteSource} is returned, rather than round-trip encoding. Subclasses that
	 * override this method should behave the same way.
	 */
	public CharSource asCharSource(Charset charset) {
		return new AsCharSource(charset);
	}

	/**
	 * Opens a new {@link InputStream} for reading from this source. This method returns a
	 * new, independent stream each time it is called.
	 *
	 * <p>
	 * The caller is responsible for ensuring that the returned stream is closed.
	 * @throws IOException if an I/O error occurs while opening the stream
	 */
	public abstract InputStream openStream() throws IOException;

	/**
	 * Opens a new buffered {@link InputStream} for reading from this source. The returned
	 * stream is not required to be a {@link BufferedInputStream} in order to allow
	 * implementations to simply delegate to {@link #openStream()} when the stream
	 * returned by that method does not benefit from additional buffering (for example, a
	 * {@code ByteArrayInputStream}). This method returns a new, independent stream each
	 * time it is called.
	 *
	 * <p>
	 * The caller is responsible for ensuring that the returned stream is closed.
	 * @throws IOException if an I/O error occurs while opening the stream
	 * @since 15.0 (in 14.0 with return type {@link BufferedInputStream})
	 */
	public InputStream openBufferedStream() throws IOException {
		InputStream in = openStream();
		return (in instanceof BufferedInputStream) ? (BufferedInputStream) in : new BufferedInputStream(in);
	}

	/**
	 * Returns a view of a slice of this byte source that is at most {@code length} bytes
	 * long starting at the given {@code offset}. If {@code offset} is greater than the
	 * size of this source, the returned source will be empty. If {@code offset + length}
	 * is greater than the size of this source, the returned source will contain the slice
	 * starting at {@code offset} and ending at the end of this source.
	 * @throws IllegalArgumentException if {@code offset} or {@code length} is negative
	 */
	public ByteSource slice(long offset, long length) {
		return new SlicedByteSource(offset, length);
	}

	/**
	 * Returns whether the source has zero bytes. The default implementation first checks
	 * {@link #sizeIfKnown}, returning true if it's known to be zero and false if it's
	 * known to be non-zero. If the size is not known, it falls back to opening a stream
	 * and checking for EOF.
	 *
	 * <p>
	 * Note that, in cases where {@code sizeIfKnown} returns zero, it is <i>possible</i>
	 * that bytes are actually available for reading. (For example, some special files may
	 * return a size of 0 despite actually having content when read.) This means that a
	 * source may return {@code true} from {@code isEmpty()} despite having readable
	 * content.
	 * @throws IOException if an I/O error occurs
	 * @since 15.0
	 */
	public boolean isEmpty() throws IOException {
		org.magneton.core.base.Optional<Long> sizeIfKnown = sizeIfKnown();
		if (sizeIfKnown.isPresent()) {
			return sizeIfKnown.get() == 0L;
		}
		org.magneton.core.io.Closer closer = org.magneton.core.io.Closer.create();
		try {
			InputStream in = closer.register(openStream());
			return in.read() == -1;
		}
		catch (Throwable e) {
			throw closer.rethrow(e);
		}
		finally {
			closer.close();
		}
	}

	/**
	 * Returns the size of this source in bytes, if the size can be easily determined
	 * without actually opening the data stream.
	 *
	 * <p>
	 * The default implementation returns {@link org.magneton.core.base.Optional#absent}.
	 * Some sources, such as a file, may return a non-absent value. Note that in such
	 * cases, it is <i>possible</i> that this method will return a different number of
	 * bytes than would be returned by reading all of the bytes (for example, some special
	 * files may return a size of 0 despite actually having content when read).
	 *
	 * <p>
	 * Additionally, for mutable sources such as files, a subsequent read may return a
	 * different number of bytes if the contents are changed.
	 *
	 * @since 19.0
	 */

	public org.magneton.core.base.Optional<Long> sizeIfKnown() {
		return org.magneton.core.base.Optional.absent();
	}

	/**
	 * Returns the size of this source in bytes, even if doing so requires opening and
	 * traversing an entire stream. To avoid a potentially expensive operation, see
	 * {@link #sizeIfKnown}.
	 *
	 * <p>
	 * The default implementation calls {@link #sizeIfKnown} and returns the value if
	 * present. If absent, it will fall back to a heavyweight operation that will open a
	 * stream, read (or {@link InputStream#skip(long) skip}, if possible) to the end of
	 * the stream and return the total number of bytes that were read.
	 *
	 * <p>
	 * Note that for some sources that implement {@link #sizeIfKnown} to provide a more
	 * efficient implementation, it is <i>possible</i> that this method will return a
	 * different number of bytes than would be returned by reading all of the bytes (for
	 * example, some special files may return a size of 0 despite actually having content
	 * when read).
	 *
	 * <p>
	 * In either case, for mutable sources such as files, a subsequent read may return a
	 * different number of bytes if the contents are changed.
	 * @throws IOException if an I/O error occurs while reading the size of this source
	 */
	public long size() throws IOException {
		org.magneton.core.base.Optional<Long> sizeIfKnown = sizeIfKnown();
		if (sizeIfKnown.isPresent()) {
			return sizeIfKnown.get();
		}

		org.magneton.core.io.Closer closer = org.magneton.core.io.Closer.create();
		try {
			InputStream in = closer.register(openStream());
			return countBySkipping(in);
		}
		catch (IOException e) {
			// skip may not be supported... at any rate, try reading
		}
		finally {
			closer.close();
		}

		closer = org.magneton.core.io.Closer.create();
		try {
			InputStream in = closer.register(openStream());
			return ByteStreams.exhaust(in);
		}
		catch (Throwable e) {
			throw closer.rethrow(e);
		}
		finally {
			closer.close();
		}
	}

	/** Counts the bytes in the given input stream using skip if possible. */
	private long countBySkipping(InputStream in) throws IOException {
		long count = 0;
		long skipped;
		while ((skipped = ByteStreams.skipUpTo(in, Integer.MAX_VALUE)) > 0) {
			count += skipped;
		}
		return count;
	}

	/**
	 * Copies the contents of this byte source to the given {@code OutputStream}. Does not
	 * close {@code output}.
	 * @return the number of bytes copied
	 * @throws IOException if an I/O error occurs while reading from this source or
	 * writing to {@code
	 *     output}
	 */
	@CanIgnoreReturnValue
	public long copyTo(OutputStream output) throws IOException {
		Preconditions.checkNotNull(output);

		org.magneton.core.io.Closer closer = org.magneton.core.io.Closer.create();
		try {
			InputStream in = closer.register(openStream());
			return ByteStreams.copy(in, output);
		}
		catch (Throwable e) {
			throw closer.rethrow(e);
		}
		finally {
			closer.close();
		}
	}

	/**
	 * Copies the contents of this byte source to the given {@code ByteSink}.
	 * @return the number of bytes copied
	 * @throws IOException if an I/O error occurs while reading from this source or
	 * writing to {@code
	 *     sink}
	 */
	@CanIgnoreReturnValue
	public long copyTo(ByteSink sink) throws IOException {
		Preconditions.checkNotNull(sink);

		org.magneton.core.io.Closer closer = org.magneton.core.io.Closer.create();
		try {
			InputStream in = closer.register(openStream());
			OutputStream out = closer.register(sink.openStream());
			return ByteStreams.copy(in, out);
		}
		catch (Throwable e) {
			throw closer.rethrow(e);
		}
		finally {
			closer.close();
		}
	}

	/**
	 * Reads the full contents of this byte source as a byte array.
	 * @throws IOException if an I/O error occurs while reading from this source
	 */
	public byte[] read() throws IOException {
		org.magneton.core.io.Closer closer = org.magneton.core.io.Closer.create();
		try {
			InputStream in = closer.register(openStream());
			org.magneton.core.base.Optional<Long> size = sizeIfKnown();
			return size.isPresent() ? ByteStreams.toByteArray(in, size.get()) : ByteStreams.toByteArray(in);
		}
		catch (Throwable e) {
			throw closer.rethrow(e);
		}
		finally {
			closer.close();
		}
	}

	/**
	 * Reads the contents of this byte source using the given {@code processor} to process
	 * bytes as they are read. Stops when all bytes have been read or the consumer returns
	 * {@code false}. Returns the result produced by the processor.
	 * @throws IOException if an I/O error occurs while reading from this source or if
	 * {@code
	 *     processor} throws an {@code IOException}
	 * @since 16.0
	 */

	@CanIgnoreReturnValue // some processors won't return a useful result
	public <T> T read(ByteProcessor<T> processor) throws IOException {
		Preconditions.checkNotNull(processor);

		org.magneton.core.io.Closer closer = org.magneton.core.io.Closer.create();
		try {
			InputStream in = closer.register(openStream());
			return ByteStreams.readBytes(in, processor);
		}
		catch (Throwable e) {
			throw closer.rethrow(e);
		}
		finally {
			closer.close();
		}
	}

	/**
	 * Hashes the contents of this byte source using the given hash function.
	 * @throws IOException if an I/O error occurs while reading from this source
	 */
	public org.magneton.core.hash.HashCode hash(org.magneton.core.hash.HashFunction hashFunction) throws IOException {
		Hasher hasher = hashFunction.newHasher();
		copyTo(Funnels.asOutputStream(hasher));
		return hasher.hash();
	}

	/**
	 * Checks that the contents of this byte source are equal to the contents of the given
	 * byte source.
	 * @throws IOException if an I/O error occurs while reading from this source or
	 * {@code other}
	 */
	public boolean contentEquals(ByteSource other) throws IOException {
		Preconditions.checkNotNull(other);

		byte[] buf1 = ByteStreams.createBuffer();
		byte[] buf2 = ByteStreams.createBuffer();

		org.magneton.core.io.Closer closer = org.magneton.core.io.Closer.create();
		try {
			InputStream in1 = closer.register(openStream());
			InputStream in2 = closer.register(other.openStream());
			while (true) {
				int read1 = ByteStreams.read(in1, buf1, 0, buf1.length);
				int read2 = ByteStreams.read(in2, buf2, 0, buf2.length);
				if (read1 != read2 || !Arrays.equals(buf1, buf2)) {
					return false;
				}
				else if (read1 != buf1.length) {
					return true;
				}
			}
		}
		catch (Throwable e) {
			throw closer.rethrow(e);
		}
		finally {
			closer.close();
		}
	}

	private static class ByteArrayByteSource extends ByteSource {

		final byte[] bytes;

		final int offset;

		final int length;

		ByteArrayByteSource(byte[] bytes) {
			this(bytes, 0, bytes.length);
		}

		// NOTE: Preconditions are enforced by slice, the only non-trivial caller.
		ByteArrayByteSource(byte[] bytes, int offset, int length) {
			this.bytes = bytes;
			this.offset = offset;
			this.length = length;
		}

		@Override
		public InputStream openStream() {
			return new ByteArrayInputStream(bytes, offset, length);
		}

		@Override
		public InputStream openBufferedStream() throws IOException {
			return openStream();
		}

		@Override
		public boolean isEmpty() {
			return length == 0;
		}

		@Override
		public long size() {
			return length;
		}

		@Override
		public org.magneton.core.base.Optional<Long> sizeIfKnown() {
			return org.magneton.core.base.Optional.of((long) length);
		}

		@Override
		public byte[] read() {
			return Arrays.copyOfRange(bytes, offset, offset + length);
		}

		// it doesn't matter what processBytes
		// returns here
		@Override
		@ParametricNullness
		public <T> T read(ByteProcessor<T> processor) throws IOException {
			processor.processBytes(bytes, offset, length);
			return processor.getResult();
		}

		@Override
		public long copyTo(OutputStream output) throws IOException {
			output.write(bytes, offset, length);
			return length;
		}

		@Override
		public HashCode hash(HashFunction hashFunction) throws IOException {
			return hashFunction.hashBytes(bytes, offset, length);
		}

		@Override
		public ByteSource slice(long offset, long length) {
			Preconditions.checkArgument(offset >= 0, "offset (%s) may not be negative", offset);
			Preconditions.checkArgument(length >= 0, "length (%s) may not be negative", length);

			offset = Math.min(offset, this.length);
			length = Math.min(length, this.length - offset);
			int newOffset = this.offset + (int) offset;
			return new ByteArrayByteSource(bytes, newOffset, (int) length);
		}

		@Override
		public String toString() {
			return "ByteSource.wrap(" + Ascii.truncate(BaseEncoding.base16().encode(bytes, offset, length), 30, "...")
					+ ")";
		}

	}

	private static final class EmptyByteSource extends ByteArrayByteSource {

		static final EmptyByteSource INSTANCE = new EmptyByteSource();

		EmptyByteSource() {
			super(new byte[0]);
		}

		@Override
		public CharSource asCharSource(Charset charset) {
			Preconditions.checkNotNull(charset);
			return CharSource.empty();
		}

		@Override
		public byte[] read() {
			return bytes; // length is 0, no need to clone
		}

		@Override
		public String toString() {
			return "ByteSource.empty()";
		}

	}

	private static final class ConcatenatedByteSource extends ByteSource {

		final Iterable<? extends ByteSource> sources;

		ConcatenatedByteSource(Iterable<? extends ByteSource> sources) {
			this.sources = Preconditions.checkNotNull(sources);
		}

		@Override
		public InputStream openStream() throws IOException {
			return new MultiInputStream(sources.iterator());
		}

		@Override
		public boolean isEmpty() throws IOException {
			for (ByteSource source : sources) {
				if (!source.isEmpty()) {
					return false;
				}
			}
			return true;
		}

		@Override
		public org.magneton.core.base.Optional<Long> sizeIfKnown() {
			if (!(sources instanceof Collection)) {
				// Infinite Iterables can cause problems here. Of course, it's true that
				// most of the other
				// methods on this class also have potential problems with infinite
				// Iterables. But unlike
				// those, this method can cause issues even if the user is dealing with a
				// (finite) slice()
				// of this source, since the slice's sizeIfKnown() method needs to know
				// the size of the
				// underlying source to know what its size actually is.
				return org.magneton.core.base.Optional.absent();
			}
			long result = 0L;
			for (ByteSource source : sources) {
				org.magneton.core.base.Optional<Long> sizeIfKnown = source.sizeIfKnown();
				if (!sizeIfKnown.isPresent()) {
					return org.magneton.core.base.Optional.absent();
				}
				result += sizeIfKnown.get();
				if (result < 0) {
					// Overflow (or one or more sources that returned a negative size, but
					// all bets are off in
					// that case)
					// Can't represent anything higher, and realistically there probably
					// isn't anything that
					// can actually be done anyway with the supposed 8+ exbibytes of data
					// the source is
					// claiming to have if we get here, so just stop.
					return org.magneton.core.base.Optional.of(Long.MAX_VALUE);
				}
			}
			return Optional.of(result);
		}

		@Override
		public long size() throws IOException {
			long result = 0L;
			for (ByteSource source : sources) {
				result += source.size();
				if (result < 0) {
					// Overflow (or one or more sources that returned a negative size, but
					// all bets are off in
					// that case)
					// Can't represent anything higher, and realistically there probably
					// isn't anything that
					// can actually be done anyway with the supposed 8+ exbibytes of data
					// the source is
					// claiming to have if we get here, so just stop.
					return Long.MAX_VALUE;
				}
			}
			return result;
		}

		@Override
		public String toString() {
			return "ByteSource.concat(" + sources + ")";
		}

	}

	/**
	 * A char source that reads bytes from this source and decodes them as characters
	 * using a charset.
	 */
	class AsCharSource extends CharSource {

		final Charset charset;

		AsCharSource(Charset charset) {
			this.charset = Preconditions.checkNotNull(charset);
		}

		@Override
		public ByteSource asByteSource(Charset charset) {
			if (charset.equals(this.charset)) {
				return ByteSource.this;
			}
			return super.asByteSource(charset);
		}

		@Override
		public Reader openStream() throws IOException {
			return new InputStreamReader(ByteSource.this.openStream(), charset);
		}

		@Override
		public String read() throws IOException {
			// Reading all the data as a byte array is more efficient than the default
			// read()
			// implementation because:
			// 1. the string constructor can avoid an extra copy most of the time by
			// correctly sizing the
			// internal char array (hard to avoid using StringBuilder)
			// 2. we avoid extra copies into temporary buffers altogether
			// The downside is that this will cause us to store the file bytes in memory
			// twice for a short
			// amount of time.
			return new String(ByteSource.this.read(), charset);
		}

		@Override
		public String toString() {
			return ByteSource.this.toString() + ".asCharSource(" + charset + ")";
		}

	}

	/** A view of a subsection of the containing byte source. */
	private final class SlicedByteSource extends ByteSource {

		final long offset;

		final long length;

		SlicedByteSource(long offset, long length) {
			Preconditions.checkArgument(offset >= 0, "offset (%s) may not be negative", offset);
			Preconditions.checkArgument(length >= 0, "length (%s) may not be negative", length);
			this.offset = offset;
			this.length = length;
		}

		@Override
		public InputStream openStream() throws IOException {
			return sliceStream(ByteSource.this.openStream());
		}

		@Override
		public InputStream openBufferedStream() throws IOException {
			return sliceStream(ByteSource.this.openBufferedStream());
		}

		private InputStream sliceStream(InputStream in) throws IOException {
			if (offset > 0) {
				long skipped;
				try {
					skipped = ByteStreams.skipUpTo(in, offset);
				}
				catch (Throwable e) {
					org.magneton.core.io.Closer closer = Closer.create();
					closer.register(in);
					try {
						throw closer.rethrow(e);
					}
					finally {
						closer.close();
					}
				}

				if (skipped < offset) {
					// offset was beyond EOF
					in.close();
					return new ByteArrayInputStream(new byte[0]);
				}
			}
			return ByteStreams.limit(in, length);
		}

		@Override
		public ByteSource slice(long offset, long length) {
			Preconditions.checkArgument(offset >= 0, "offset (%s) may not be negative", offset);
			Preconditions.checkArgument(length >= 0, "length (%s) may not be negative", length);
			long maxLength = this.length - offset;
			return maxLength <= 0 ? ByteSource.empty()
					: ByteSource.this.slice(this.offset + offset, Math.min(length, maxLength));
		}

		@Override
		public boolean isEmpty() throws IOException {
			return length == 0 || super.isEmpty();
		}

		@Override
		public org.magneton.core.base.Optional<Long> sizeIfKnown() {
			org.magneton.core.base.Optional<Long> optionalUnslicedSize = ByteSource.this.sizeIfKnown();
			if (optionalUnslicedSize.isPresent()) {
				long unslicedSize = optionalUnslicedSize.get();
				long off = Math.min(offset, unslicedSize);
				return org.magneton.core.base.Optional.of(Math.min(length, unslicedSize - off));
			}
			return org.magneton.core.base.Optional.absent();
		}

		@Override
		public String toString() {
			return ByteSource.this.toString() + ".slice(" + offset + ", " + length + ")";
		}

	}

}
