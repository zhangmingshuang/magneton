package org.magneton.test.mock;

/**
 * .
 *
 * @author zhangmsh 2021/11/19
 * @since 2.0.3
 */
public class UnsupportedException extends RuntimeException {

	public static final UnsupportedException INSTANCE = new UnsupportedException();

	private static final long serialVersionUID = -2657719512352019241L;

	public UnsupportedException() {
		super("暂不支持该方法");
	}

}
