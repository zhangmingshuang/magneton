package org.magneton.foundation;

import org.magneton.foundation.exception.MkdirException;
import org.magneton.foundation.exception.WriteFileException;

import java.io.*;

/**
 * 文件工具类
 *
 * @author zhangmsh
 * @since 1.0.0
 */
public class MoreFiles {

	private static final int WRITE_BUFF_SIZE = 8192;

	private MoreFiles() {
	}

	/**
	 * Reads the contents of a file into a byte array. * The file is always closed.
	 * @param file the file to read
	 * @return the file contents or an empty byte array if the file is empty
	 * @throws IOException in case of an I/O error
	 */
	public static byte[] readFileToByteArray(final File file) throws IOException {
		try (InputStream in = openInputStream(file)) {
			final long fileLength = file.length();
			return fileLength > 0 ? IoUtils.toByteArray(in, (int) fileLength) : IoUtils.toByteArray(in);
		}
	}

	/**
	 * Opens a {@link FileInputStream} for the specified file, providing better error
	 * messages than simply calling <code>new FileInputStream(file)</code>.
	 *
	 * <p>
	 * At the end of the method either the stream will be successfully opened, or an
	 * exception will have been thrown.
	 *
	 * <p>
	 * An exception is thrown if the file does not exist. An exception is thrown if the
	 * file object exists but is a directory. An exception is thrown if the file exists
	 * but cannot be read.
	 * @param file the file to open for input, must not be {@code null}
	 * @return a new {@link FileInputStream} for the specified file
	 * @throws IOException if the file does not exist
	 */
	public static FileInputStream openInputStream(final File file) throws IOException {
		if (file.exists()) {
			if (file.isDirectory()) {
				throw new IOException("File '" + file + "' exists but is a directory");
			}
			if (!file.canRead()) {
				throw new IOException("File '" + file + "' cannot be read");
			}
		}
		else {
			throw new FileNotFoundException("File '" + file + "' does not exist");
		}
		return new FileInputStream(file);
	}

	/**
	 * Write inputStream to file
	 * @param file the file to write to
	 * @param inputStream the input stream
	 */
	public static void writeToFile(File file, InputStream inputStream) {
		try (OutputStream outputStream = new FileOutputStream(file);) {
			int bytesRead;
			byte[] buffer = new byte[WRITE_BUFF_SIZE];
			while ((bytesRead = inputStream.read(buffer, 0, WRITE_BUFF_SIZE)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
			}
		}
		catch (Exception e) {
			throw new WriteFileException("Can not create temporary file!", e);
		}
		finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				}
				catch (IOException e) {
					throw new WriteFileException("Can not close 'inputStream'", e);
				}
			}
		}
	}

	private static File createDirectory(File directory) {
		if (!directory.exists() && !directory.mkdirs()) {
			throw new MkdirException("Cannot create directory:" + directory.getAbsolutePath());
		}
		return directory;
	}

	/**
	 * delete file
	 * @param file file
	 */
	@SuppressWarnings("ResultOfMethodCallIgnored")
	public static void delete(File file) {
		if (file.isFile()) {
			file.delete();
			return;
		}
		if (file.isDirectory()) {
			File[] childFiles = file.listFiles();
			if (childFiles == null || childFiles.length == 0) {
				file.delete();
				return;
			}
			for (int i = 0; i < childFiles.length; i++) {
				delete(childFiles[i]);
			}
			file.delete();
		}
	}

}