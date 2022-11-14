package org.magneton.test.mock;

/**
 * 不支持异常.
 *
 * @author zhangmsh 2021/11/19
 * @since 2.0.3
 */
public class UnsupportedException extends RuntimeException {

	/**
	 * 异常单例
	 */
	public static final UnsupportedException INSTANCE = new UnsupportedException();

	private static final long serialVersionUID = -2657719512352019241L;

	public UnsupportedException() {
		super("暂不支持该方法");
	}

}
