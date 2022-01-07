package org.magneton.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.magneton.core.exception.MkdirException;
import org.magneton.core.exception.WriteFileException;

/**
 * .
 *
 * @author zhangmsh
 * @version 1.0.0
 * @since 2021/09/07
 */
public class MoreFiles {

	private static final int WRITE_BUFF_SIZE = 8192;

	private MoreFiles() {
	}

	/**
	 * Reads the contents of a file into a byte array. * The file is always closed.
	 * @param file
	 * @return
	 * @throws IOException
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
	 * @param file
	 * @return
	 * @throws IOException
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
	 * @param file
	 * @param inputStream
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
		if (!directory.exists() && !directory.mkdirs())
			throw new MkdirException("Cannot create directory:" + directory.getAbsolutePath());
		return directory;
	}

	/**
	 * delete file
	 * @param file
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
