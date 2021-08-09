package cn.nascent.framework.test.exception;

/**
 * .
 *
 * @author zhangmsh 2021/8/2
 * @since 2.0.0
 */
public class NoSupportTypeCreateException extends RuntimeException {

	private static final long serialVersionUID = -2070524012863299399L;

	public NoSupportTypeCreateException(String message) {
		super(message);
	}

}
